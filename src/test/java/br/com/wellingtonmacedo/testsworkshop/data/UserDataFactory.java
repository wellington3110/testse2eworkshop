package br.com.wellingtonmacedo.testsworkshop.data;

import br.com.wellingtonmacedo.testsworkshop.component.CPF;
import br.com.wellingtonmacedo.testsworkshop.dto.CreateUserDTO;
import br.com.wellingtonmacedo.testsworkshop.dto.ErrorDTO;
import com.github.javafaker.Faker;

import java.time.LocalDate;

public class UserDataFactory {

    private static final Faker faker = new Faker();

    public static CreateUserDTO createValidUser() {
        return new CreateUserDTO()
            .setCpf(CPF.generate())
            .setName(faker.howIMetYourMother().character())
            .setBirthDate(LocalDate.now())
            .setAddress(faker.address().fullAddress())
            .setEmail(faker.internet().emailAddress());
    }

    public static CreateUserDTO createMinimumValidUserToSave() {
        return new CreateUserDTO()
            .setCpf(CPF.generate())
            .setName(faker.name().fullName())
            .setBirthDate(LocalDate.now());
    }

    public static UserAndError createEmptyUser() {
        CreateUserDTO userToSave = new CreateUserDTO();
        ErrorDTO expectedError = ErrorDataFactory
            .validationError()
            .mustNotBeBlank("name")
            .mustNotBeNull("birthDate")
            .mustNotBeNull("cpf")
            .build();
        return new UserAndError(userToSave, expectedError);
    }

    public static UserAndError createUserWithAllFieldsInvalid() {
        CreateUserDTO userToSave = new CreateUserDTO()
            .setEmail("Goku > Naruto")
            .setAddress("_")
            .setCpf("bolacha ou biscoito?")
            .setName("_")
            .setBirthDate(LocalDate.now().plusDays(1));
        ErrorDTO expectedError = ErrorDataFactory
            .validationError()
            .invalidCpf()
            .invalidEmail()
            .invalidSize("address", 5, 254)
            .invalidSize("name", 3, 90)
            .mustBeADateInThePastOrInThePresent("birthDate")
            .build();
        return new UserAndError(userToSave, expectedError);
    }

    public static class UserAndError {
        public final CreateUserDTO user;
        public final ErrorDTO error;

        public UserAndError(CreateUserDTO user, ErrorDTO error) {
            this.user = user;
            this.error = error;
        }
    }
}
