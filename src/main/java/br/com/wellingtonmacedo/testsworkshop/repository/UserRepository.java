package br.com.wellingtonmacedo.testsworkshop.repository;

import br.com.wellingtonmacedo.testsworkshop.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Boolean existsByCpf(String cpf);
}
