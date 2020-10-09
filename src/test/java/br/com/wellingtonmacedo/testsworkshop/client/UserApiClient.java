package br.com.wellingtonmacedo.testsworkshop.client;

import br.com.wellingtonmacedo.testsworkshop.dto.CreateUserDTO;
import br.com.wellingtonmacedo.testsworkshop.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserApiClient {

    private static final String RESOURCE = "/user";

    @Autowired
    private TestRestTemplate restTemplate;

    public ResponseEntity<Void> save(CreateUserDTO dto) {
        return save(dto, Void.class);
    }

    public <T> ResponseEntity<T> save(CreateUserDTO dto, Class<T> responseType) {
        return restTemplate.postForEntity(RESOURCE, dto, responseType);
    }

    public ResponseEntity<UserDTO> getById(Long id) {
        return restTemplate.getForEntity(RESOURCE + "/" + id, UserDTO.class);
    }
}
