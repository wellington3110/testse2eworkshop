package br.com.wellingtonmacedo.testsworkshop.dto;

import br.com.wellingtonmacedo.testsworkshop.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDate;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {

    private String cpf;
    private String name;
    private String email;
    private String address;
    private LocalDate birthDate;

    public UserEntity toEntity() {
        return new UserEntity()
            .setCpf(cpf)
            .setName(name)
            .setEmail(email)
            .setAddress(address)
            .setBirthDate(birthDate);
    }
}
