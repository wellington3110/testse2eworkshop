package br.com.wellingtonmacedo.testsworkshop.it;

import br.com.wellingtonmacedo.testsworkshop.AbstractIT;
import br.com.wellingtonmacedo.testsworkshop.client.UserApiClient;
import br.com.wellingtonmacedo.testsworkshop.component.UUIDGenerator;
import br.com.wellingtonmacedo.testsworkshop.data.ErrorDataFactory;
import br.com.wellingtonmacedo.testsworkshop.data.UserEventDataFactory;
import br.com.wellingtonmacedo.testsworkshop.dto.CreateUserDTO;
import br.com.wellingtonmacedo.testsworkshop.dto.ErrorDTO;
import br.com.wellingtonmacedo.testsworkshop.dto.UserDTO;
import br.com.wellingtonmacedo.testsworkshop.events.UserRegisteredEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static br.com.wellingtonmacedo.testsworkshop.data.UserDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import java.util.Objects;
import java.util.UUID;

class UserIT extends AbstractIT {

    private static String PUBLISHER_URI = "http://mock.publisher/publish";
    private static UUID DEFAULT_UUID = UUID.randomUUID();

    @Autowired
    private UserApiClient userApiClient;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private UUIDGenerator uuidGenerator;

    @BeforeEach
    void setUp() {
        when(uuidGenerator.generate()).thenReturn(DEFAULT_UUID);
    }

    @Test
    @DisplayName("Save and get user with all fields filled successfully")
    void allFieldsFilledProperly() {
        assertSaveUserSuccessfully(createValidUser());
    }

    @Test
    @DisplayName("Save and get user with just required fields filled successfully")
    void requiredFieldsFilled() {
        CreateUserDTO minimumValidUserToSave = createMinimumValidUserToSave();
        assertSaveUserSuccessfully(minimumValidUserToSave);
    }

    @Test
    @DisplayName("Error to save user without any field filled")
    void noFieldFilled() {
        UserAndError userAndError = createEmptyUser();
        assertValidationError(userAndError);
    }

    @Test
    @DisplayName("Error to save user with invalid e-mail, address, name, cpf and birth date")
    void invalidEmailAddressNameCpfAndBirthDate() {
        UserAndError userAndError = createUserWithAllFieldsInvalid();
        assertValidationError(userAndError);
    }

    @Test
    @DisplayName("Error to save user with invalid e-mail and repeated cpf")
    void invalidEmailAndRepeatedCpf() {
        CreateUserDTO validUser = createValidUser();
        assertSaveUserSuccessfully(validUser);
        CreateUserDTO invalidUser = validUser.withEmail("ronaldo");
        ErrorDTO expectedError = ErrorDataFactory
            .validationError()
            .repeatedField("cpf")
            .invalidEmail()
            .build();
        assertValidationError(invalidUser, expectedError);
    }

    private void assertSaveUserSuccessfully(CreateUserDTO userToSave) {
        ResponseEntity<Void> resp = userApiClient.save(userToSave);
        assertThat(resp.getStatusCode()).isEqualTo(CREATED);
        UserDTO savedUser = UserDTO.fromDTO(userToSave, getUserId(resp));
        UserRegisteredEvent expectedEvent = UserEventDataFactory.toUserRegisteredEvent(savedUser, DEFAULT_UUID);
        verify(restTemplate).postForEntity(PUBLISHER_URI, expectedEvent, String.class);
        assertGetByID(savedUser);
    }

    private void assertValidationError(UserAndError userAndError) {
        assertValidationError(userAndError.user, userAndError.error);
    }

    private void assertValidationError(CreateUserDTO user, ErrorDTO error) {
        ResponseEntity<ErrorDTO> resp = userApiClient.save(user, ErrorDTO.class);
        assertThat(resp.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo(error);
        verifyNoMoreInteractions(restTemplate);
    }

    private void assertGetByID(UserDTO expected) {
        ResponseEntity<UserDTO> resp = userApiClient.getById(expected.getId());
        assertThat(resp.getStatusCode()).isEqualTo(OK);
        assertThat(resp.getBody()).isEqualTo(expected);
    }

    private Long getUserId(ResponseEntity<Void> resp) {
        return Long.valueOf(Objects.requireNonNull(resp.getHeaders().get("Location-id")).get(0));
    }
}
