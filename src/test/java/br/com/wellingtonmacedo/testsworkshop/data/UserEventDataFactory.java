package br.com.wellingtonmacedo.testsworkshop.data;

import br.com.wellingtonmacedo.testsworkshop.dto.UserDTO;
import br.com.wellingtonmacedo.testsworkshop.events.UserRegisteredEvent;

import java.util.UUID;

public class UserEventDataFactory {

    public static UserRegisteredEvent toUserRegisteredEvent(UserDTO dto, UUID id) {
        return UserRegisteredEvent
            .builder()
            .id(id)
            .cpf(dto.getCpf())
            .email(dto.getEmail())
            .name(dto.getName())
            .build();
    }
}
