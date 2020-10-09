package br.com.wellingtonmacedo.testsworkshop.it;

import br.com.wellingtonmacedo.testsworkshop.AbstractIT;
import br.com.wellingtonmacedo.testsworkshop.client.UserApiClient;
import br.com.wellingtonmacedo.testsworkshop.component.CPF;
import br.com.wellingtonmacedo.testsworkshop.component.UUIDGenerator;
import br.com.wellingtonmacedo.testsworkshop.dto.CreateUserDTO;
import br.com.wellingtonmacedo.testsworkshop.dto.ErrorDTO;
import br.com.wellingtonmacedo.testsworkshop.dto.UserDTO;
import br.com.wellingtonmacedo.testsworkshop.events.UserRegisteredEvent;
import br.com.wellingtonmacedo.testsworkshop.exception.ValidationException;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

class UserIT extends AbstractIT {

    private static String PUBLISHER_URI = "http://mock.publisher/publish";
    private static UUID DEFAULT_UUID = UUID.randomUUID();

    private final Faker faker = new Faker();

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
        CreateUserDTO userToSave = new CreateUserDTO()
            .setCpf(CPF.generate())
            .setName(faker.howIMetYourMother().character())
            .setBirthDate(LocalDate.now())
            .setAddress(faker.address().fullAddress())
            .setEmail(faker.internet().emailAddress());
        assertSaveUserSuccessfully(userToSave);
    }

    @Test
    @DisplayName("Save and get user with just required fields filled successfully")
    void requiredFieldsFilled() {
        CreateUserDTO userToSave = new CreateUserDTO()
            .setCpf(CPF.generate())
            .setName(faker.name().fullName())
            .setBirthDate(LocalDate.now());
        UserDTO savedUser = assertSaveUserSuccessfully(userToSave);
        assertGetByID(savedUser);
    }

    @Test
    @DisplayName("Error to save user without any field filled")
    void noFieldFilled() {
        CreateUserDTO userToSave = new CreateUserDTO();
        ErrorDTO expectedError = new ErrorDTO()
            .setCode(ValidationException.CODE_ERROR)
            .addError("name", "must not be blank")
            .addError("birthDate", "must not be null");
        ResponseEntity<ErrorDTO> resp = userApiClient.save(userToSave, ErrorDTO.class);
        verifyNoInteractions(restTemplate);
        assertThat(resp.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo(expectedError);
    }

    @Test
    @DisplayName("Error to save user with invalid e-mail, address, name, cpf and birth date")
    void invalidEmailAddressNameCpfAndBirthDate() {
        CreateUserDTO userToSave = new CreateUserDTO()
            .setEmail("Goku > Naruto")
            .setAddress("_")
            .setCpf("bolacha ou biscoito?")
            .setName("_")
            .setBirthDate(LocalDate.now().plusDays(1));
        ErrorDTO expectedError = new ErrorDTO()
            .setCode(ValidationException.CODE_ERROR)
            .addError("address", "size must be between 5 and 254")
            .addError("name", "size must be between 3 and 90")
            .addError("cpf", "invalid Brazilian individual taxpayer registry number (CPF)")
            .addError("email", "must be a well-formed email address")
            .addError("birthDate", "must be a date in the past or in the present");
        ResponseEntity<ErrorDTO> resp = userApiClient.save(userToSave, ErrorDTO.class);
        verifyNoInteractions(restTemplate);
        assertThat(resp.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo(expectedError);
    }

    @Test
    @DisplayName("Error to save user with invalid e-mail and repeated cpf")
    void invalidEmailAndRepeatedCpf() {
        CreateUserDTO validUser = new CreateUserDTO()
            .setCpf(CPF.generate())
            .setName(faker.howIMetYourMother().character())
            .setBirthDate(LocalDate.now())
            .setAddress(faker.address().fullAddress())
            .setEmail(faker.internet().emailAddress());
        assertSaveUserSuccessfully(validUser);
        CreateUserDTO invalidUser = validUser.withEmail("ronaldo");
        ErrorDTO expectedError = new ErrorDTO()
            .setCode(ValidationException.CODE_ERROR)
            .addError("email", "must be a well-formed email address")
            .addError("cpf", "value already registered");
        ResponseEntity<ErrorDTO> resp = userApiClient.save(invalidUser, ErrorDTO.class);
        assertThat(resp.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo(expectedError);
    }

    private UserDTO assertSaveUserSuccessfully(CreateUserDTO userToSave) {
        ResponseEntity<Void> resp = userApiClient.save(userToSave);
        assertThat(resp.getStatusCode()).isEqualTo(CREATED);
        UserDTO savedUser = UserDTO.fromDTO(userToSave, getUserId(resp));
        UserRegisteredEvent expectedEvent = toEvent(savedUser);
        verify(restTemplate).postForEntity(PUBLISHER_URI, expectedEvent, String.class);
        return assertGetByID(savedUser);
    }

    private UserRegisteredEvent toEvent(UserDTO savedUser) {
        return UserRegisteredEvent
            .builder()
            .id(DEFAULT_UUID)
            .cpf(savedUser.getCpf())
            .email(savedUser.getEmail())
            .name(savedUser.getName())
            .build();
    }

    private UserDTO assertGetByID(UserDTO expected) {
        ResponseEntity<UserDTO> resp = userApiClient.getById(expected.getId());
        assertThat(resp.getStatusCode()).isEqualTo(OK);
        assertThat(resp.getBody()).isEqualTo(expected);
        return expected;
    }

    private Long getUserId(ResponseEntity<Void> resp) {
        return Long.valueOf(Objects.requireNonNull(resp.getHeaders().get("Location-id")).get(0));
    }
}
