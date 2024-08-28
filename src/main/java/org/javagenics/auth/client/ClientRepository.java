package org.javagenics.auth.client;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByEmail(String email);

    Optional<ClientEntity> findByCpf(String cpf);

    Optional<ClientEntity> findByEmailOrCpf(String email, String cpf);
}
