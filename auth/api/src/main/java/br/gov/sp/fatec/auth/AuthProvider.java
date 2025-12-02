package br.gov.sp.fatec.auth;

import br.gov.sp.fatec.model.TokenMetadata;
import br.gov.sp.fatec.services.AuthService;
import br.gov.sp.fatec.services.TokenService;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthProvider implements AuthenticationProvider {
    private final AuthService authService;

    private final TokenService tokenService;

    public AuthProvider(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var user = authService.login(
                authentication.getName(),
                authentication.getCredentials().toString()
        );

        var userDetails = new UserDetailsAdapter(user);

        var token = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        var jwtToken = tokenService.create(user);

        token.setDetails(jwtToken);

        return token;
    }

    // Indica se este provedor de autenticação suporta o tipo de autenticação fornecido
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
