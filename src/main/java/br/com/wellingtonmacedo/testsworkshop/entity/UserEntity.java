package br.com.wellingtonmacedo.testsworkshop.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "user")
@Entity
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String address;
    @Column(name = "birth_date")
    private LocalDate birthDate;
}
