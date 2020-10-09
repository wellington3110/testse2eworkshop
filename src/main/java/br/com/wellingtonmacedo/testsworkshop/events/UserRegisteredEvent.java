package br.com.wellingtonmacedo.testsworkshop.events;

import br.com.wellingtonmacedo.testsworkshop.entity.UserEntity;
import lombok.*;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserRegisteredEvent extends Event {

    private static final String TYPE = "UserRegistered";

    private final String name;
    private final String cpf;
    private final String email;

    @Builder
    private UserRegisteredEvent(UUID id, String name, String cpf, String email) {
        super(id, TYPE);
        this.name = name;
        this.cpf = cpf;
        this.email = email;
    }

    public static UserRegisteredEvent from(UserEntity user) {
        return UserRegisteredEvent
            .builder()
            .name(user.getName())
            .cpf(user.getCpf())
            .email(user.getEmail())
            .build();
    }

    @Override
    public Event withId(UUID id) {
        return new UserRegisteredEvent(id, name, cpf, email);
    }
}
