package br.com.wellingtonmacedo.testsworkshop.controller;

import br.com.wellingtonmacedo.testsworkshop.dto.CreateUserDTO;
import br.com.wellingtonmacedo.testsworkshop.dto.UserDTO;
import br.com.wellingtonmacedo.testsworkshop.entity.UserEntity;
import br.com.wellingtonmacedo.testsworkshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(UserController.RESOURCE)
public class UserController {

    private final UserService userService;
    static final String RESOURCE = "/user";

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CreateUserDTO dto) {
        UserEntity savedUser = userService.save(dto.toEntity());
        return ResponseEntity
            .created(URI.create(RESOURCE + "/" + savedUser.getId()))
            .header("Location-id", savedUser.getId().toString())
            .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") Long id) {
        Optional<UserEntity> user = userService.getById(id);
        return user
            .map(UserDTO::fromEntity)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
