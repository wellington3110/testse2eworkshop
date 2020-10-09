package br.com.wellingtonmacedo.testsworkshop.dto;

import br.com.wellingtonmacedo.testsworkshop.entity.UserEntity;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private LocalDate birthDate;
    private String cpf;
    private String email;
    private String address;

    public static UserDTO fromEntity(UserEntity entity) {
        return new UserDTO()
            .setId(entity.getId())
            .setCpf(entity.getCpf())
            .setName(entity.getName())
            .setBirthDate(entity.getBirthDate())
            .setAddress(entity.getAddress())
            .setEmail(entity.getEmail());
    }

    public static UserDTO fromDTO(CreateUserDTO dto, Long id) {
        return new UserDTO()
            .setId(id)
            .setCpf(dto.getCpf())
            .setName(dto.getName())
            .setBirthDate(dto.getBirthDate())
            .setAddress(dto.getAddress())
            .setEmail(dto.getEmail());
    }
}
