package br.com.wellingtonmacedo.testsworkshop.service;

import br.com.wellingtonmacedo.testsworkshop.config.Loggable;
import br.com.wellingtonmacedo.testsworkshop.events.Event;
import br.com.wellingtonmacedo.testsworkshop.events.UserRegisteredEvent;
import br.com.wellingtonmacedo.testsworkshop.exception.ValidationException;
import br.com.wellingtonmacedo.testsworkshop.telemetry.UserTelemetry;
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
public class UserService implements Loggable {

    private final UserRepository userRepository;
    private final UserTelemetry userTelemetry;
    private final EventService eventService;

    @Autowired
    public UserService(UserRepository userRepository, UserTelemetry userTelemetry, EventService eventService) {
        this.userRepository = userRepository;
        this.userTelemetry = userTelemetry;
        this.eventService = eventService;
    }

    public Optional<UserEntity> getById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity save(UserEntity user) {
        validate(user);
        UserEntity savedUser = userRepository.save(user);
        saveEvent(UserRegisteredEvent.from(savedUser));
        return savedUser;
    }

    private void saveEvent(Event event) {
        try {
            eventService.publish(event);
        } catch (Exception e) {
            userTelemetry.errorToSaveEvent(event, e);
        }
    }

    private void validate(UserEntity userEntity) {
        List<ErrorCause> errorCauses = new UserValidator()
            .birthDate(userEntity.getBirthDate())
            .name(userEntity.getName())
            .cpf(userEntity.getCpf())
            .email(userEntity.getEmail())
            .address(userEntity.getAddress())
            .validate();
        Optional<ErrorCause> cpfError = validateUniqueCpf(userEntity.getCpf());
        cpfError.ifPresent(errorCauses::add);
        if (!errorCauses.isEmpty()) {
            throw new ValidationException(errorCauses);
        }
    }

    private Optional<ErrorCause> validateUniqueCpf(String cpf) {
        if (userRepository.existsByCpf(cpf)) {
            return Optional.of(new ErrorCause("cpf", VALUE_ALREADY_REGISTERED));
        }
        return Optional.empty();
    }

    @Data
    @Accessors(fluent = true)
    private static class UserValidator extends DataValidator {

        @NotBlank
        @Size(min = 3, max = 90)
        private String name;

        @NotNull
        @PastOrPresent
        private LocalDate birthDate;

        @CPF
        @NotNull
        private String cpf;

        @Email
        private String email;

        @Size(min = 5, max = 254)
        private String address;
    }
}
