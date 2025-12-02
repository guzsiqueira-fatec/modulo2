package br.gov.sp.fatec.infra.database.jpa.repositories;

import br.gov.sp.fatec.infra.database.jpa.models.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryJpa extends JpaRepository<UserJpa, UUID> {
    Optional<UserJpa> findByEmail(String email);
}
