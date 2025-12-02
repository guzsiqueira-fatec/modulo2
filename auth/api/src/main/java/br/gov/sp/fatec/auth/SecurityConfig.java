package br.gov.sp.fatec.auth;

import br.gov.sp.fatec.policies.PasswordPolicy;
import br.gov.sp.fatec.repositories.UserRepository;
import br.gov.sp.fatec.services.AuthService;
import br.gov.sp.fatec.services.PasswordHasherService;
import br.gov.sp.fatec.services.TokenService;
import br.gov.sp.fatec.specifications.password.MinCharsSpec;
import br.gov.sp.fatec.specifications.password.NumericCharSpec;
import br.gov.sp.fatec.specifications.password.PasswordSpec;
import br.gov.sp.fatec.specifications.password.UpperCaseSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordPolicy passwordPolicy() {
        return (s) -> {
            var result = new MinCharsSpec(6)
                    .and(new NumericCharSpec())
                    .and(new UpperCaseSpec())
                    .test(s);
            return result == null ? List.of() : result.errors();
        };
    }

    @Bean
    public PasswordHasherService passwordHasherService(PasswordEncoder passwordEncoder) {
        return new PasswordHasherService() {
            @Override
            public String hash(String password) {
                return passwordEncoder.encode(password);
            }

            @Override
            public boolean verify(String password, String hashedPassword) {
                return passwordEncoder.matches(password, hashedPassword);
            }
        };
    }

    @Bean
    public AuthService authService(
            PasswordPolicy passwordPolicy,
            PasswordHasherService passwordHasherService,
            UserRepository userRepository
    ) {
        return new AuthService(
                passwordPolicy,
                passwordHasherService,
                userRepository
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationProvider authenticationProvider
    ) {
        return authenticationProvider::authenticate;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // esse servico não tem endpoints protegidos
                )
                .build();
    }
}
