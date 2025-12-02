package br.gov.sp.fatec.services;

import br.gov.sp.fatec.entity.User;
import br.gov.sp.fatec.exceptions.BadCredentialsException;
import br.gov.sp.fatec.exceptions.EmailAlreadyRegisteredException;
import br.gov.sp.fatec.exceptions.NonCompliantPasswordException;
import br.gov.sp.fatec.policies.PasswordPolicy;
import br.gov.sp.fatec.repositories.UserRepository;
import br.gov.sp.fatec.specifications.password.PasswordSpec;

import java.util.UUID;

public class AuthService {
    private final PasswordPolicy passwordPolicy;
    private final PasswordHasherService passwordHasherService;
    private final UserRepository userRepository;

    public AuthService(PasswordPolicy passwordPolicy, PasswordHasherService passwordHasherService, UserRepository userRepository) {
        this.passwordPolicy = passwordPolicy;
        this.passwordHasherService = passwordHasherService;
        this.userRepository = userRepository;
    }

    public User register(String name, String email, String password) {
        var violations = passwordPolicy.getViolations(password);

        if (!violations.isEmpty()) {
            var errorMessages = violations.stream()
                .map(PasswordSpec::getErrorMessage)
                .toList();
            throw new NonCompliantPasswordException("Password does not meet the policy requirements: " + String.join(", ", errorMessages));
        }

        // TODO: Refatorar para não precisar bater no banco de dados duas vezes, deixar a camada de INFRA lidar com UNIQUENESS
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyRegisteredException("Email " + email + " is already registered.");
        }

        String hashedPassword = passwordHasherService.hash(password);
        User newUser = new User(UUID.randomUUID().toString(), name, email, hashedPassword);
        return userRepository.save(newUser);
    }

    public User login(String email, String password) {
        var user = userRepository.findByEmail(email);
        if (user.isEmpty() || !passwordHasherService.verify(password, user.get().password())) {
            throw new BadCredentialsException("Invalid email or password.");
        }
        return user.get();
    }
}
