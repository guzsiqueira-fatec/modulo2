package br.gov.sp.fatec.infra.database.implementations;

import br.gov.sp.fatec.entity.User;
import br.gov.sp.fatec.infra.database.jpa.models.UserJpa;
import br.gov.sp.fatec.infra.database.jpa.repositories.UserRepositoryJpa;
import br.gov.sp.fatec.repositories.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserRepositoryJpa userRepositoryJpa;

    public UserRepositoryImpl(UserRepositoryJpa userRepositoryJpa) {
        this.userRepositoryJpa = userRepositoryJpa;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepositoryJpa.findByEmail(email).map(UserJpa::toDomain);
    }

    @Override
    public User save(User user) {
        UserJpa userJpa = UserJpa.fromDomain(user);
        UserJpa savedUserJpa = userRepositoryJpa.save(userJpa);
        return savedUserJpa.toDomain();
    }
}
