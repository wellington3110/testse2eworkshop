package br.com.wellingtonmacedo.testsworkshop.service;

import br.com.wellingtonmacedo.testsworkshop.exception.ValidationException;
import br.com.wellingtonmacedo.testsworkshop.validator.ErrorCause;
import br.com.wellingtonmacedo.testsworkshop.validator.DataValidator;
import br.com.wellingtonmacedo.testsworkshop.entity.UserEntity;
import br.com.wellingtonmacedo.testsworkshop.repository.UserRepository;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static br.com.wellingtonmacedo.testsworkshop.exception.ValidationException.VALUE_ALREADY_REGISTERED;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity save(UserEntity userEntity) {
        validate(userEntity);
        return userRepository.save(userEntity);
    }

    private void validate(UserEntity userEntity) {
        List<ErrorCause> errorCauses = new UserValidator()
            .birthDate(userEntity.getBirthDate())
            .name(userEntity.getName())
            .cpf(userEntity.getCpf())
            .email(userEntity.getEmail())
            .address(userEntity.getAddress())
            .validate();
        Optional<ErrorCause> cpfError = validateUniqueCPF(userEntity.getCpf());
        cpfError.ifPresent(errorCauses::add);
        if (!errorCauses.isEmpty()) {
            throw new ValidationException(errorCauses);
        }
    }

    private Optional<ErrorCause> validateUniqueCPF(String cpf) {
        if (userRepository.existsByCpf(cpf)) {
            return Optional.of(new ErrorCause("cpf", VALUE_ALREADY_REGISTERED));
        }
        return Optional.empty();
    }

    public Optional<UserEntity> getById(Long id) {
        return userRepository.findById(id);
    }

    @Data
    @Accessors(fluent = true, chain = true)
    private static class UserValidator extends DataValidator {

        @NotBlank
        @Size(min = 3, max = 90)
        private String name;

        @NotNull
        @PastOrPresent
        private LocalDate birthDate;

        @CPF
        private String cpf;

        @Email
        private String email;

        @Size(min = 5, max = 254)
        private String address;
    }
}
