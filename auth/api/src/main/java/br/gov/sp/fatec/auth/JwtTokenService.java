package br.gov.sp.fatec.auth;

import br.gov.sp.fatec.entity.User;
import br.gov.sp.fatec.model.Token;
import br.gov.sp.fatec.model.TokenMetadata;
import br.gov.sp.fatec.services.TokenService;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtTokenService implements TokenService {
    private final byte[] secretKey;
    private final Long expirationTime;

    public JwtTokenService(
        @Value("${application.authentication.secret-key}") byte[] secretKey,
        @Value("${application.authentication.expiration-time-in-seconds}") Long expirationTime
    ) {
        this.secretKey = secretKey;
        this.expirationTime = expirationTime;
    }

    @Override
    public Token create(User user) {
        var metadata = new TokenMetadata(
                user.id(),
                "auth-service",
                LocalDateTime.now(),
                LocalDateTime.now().plusSeconds(expirationTime),
                "users",
                user.name()
        );
        return create(metadata);
    }

    public Token create(TokenMetadata metadata) {
        var key = Keys.hmacShaKeyFor(secretKey);
        String jwt = Jwts.builder()
                .subject(metadata.subject())
                .issuer(metadata.issuer())
                .audience(

                ).add(metadata.audience())
                .and()
                .issuedAt(
                        Date.from(metadata.issuedAt().atZone(ZoneId.systemDefault()).toInstant())
                )
                .expiration(
                        Date.from(metadata.expiresAt().atZone(ZoneId.systemDefault()).toInstant())
                )
                .claim("userName", metadata.claims())
                .signWith(key, Jwts.SIG.HS256)
                .compact();
        return new Token(jwt, metadata);
    }
}
