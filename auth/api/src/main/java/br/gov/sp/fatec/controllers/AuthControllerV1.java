package br.gov.sp.fatec.controllers;

import br.gov.sp.fatec.dtos.AuthResponseDto;
import br.gov.sp.fatec.dtos.LoginRequestDto;
import br.gov.sp.fatec.dtos.RegisterRequestDto;
import br.gov.sp.fatec.model.Token;
import br.gov.sp.fatec.services.AuthService;
import br.gov.sp.fatec.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthControllerV1 {
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final TokenService tokenService;

    public AuthControllerV1(AuthenticationManager authenticationManager, AuthService authService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginRequestDto dto) {
        System.out.println("Login attempt for email: " + dto.email());
        var token = new UsernamePasswordAuthenticationToken(
                dto.email(),
                dto.password()
        );
        var authentication = authenticationManager.authenticate(token);
        var jwtToken = authentication.getDetails();
        if ((jwtToken instanceof Token jwtTokenCasted)) {
            return new AuthResponseDto(jwtTokenCasted.token());
        }
        throw new IllegalStateException("Token inválido");
    }

    @PostMapping("/register")
    public AuthResponseDto register(@Valid @RequestBody RegisterRequestDto dto) {
        var user = authService.register(dto.name(), dto.email(), dto.password());
        var jwtToken = tokenService.create(user);
        return new AuthResponseDto(jwtToken.token());
    }
}
