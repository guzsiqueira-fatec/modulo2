package br.gov.sp.fatec.repositories;

import br.gov.sp.fatec.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
}
