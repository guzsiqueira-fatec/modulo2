package br.gov.sp.fatec.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {
    private final AuthProperties authProperties;

    public JwtAuthFilter(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public int getOrder() {
        return -1;
    }

    // Faz a verificação se a rota é pública, considerando padrões com curingas /** e tals
    private boolean isPublic(String path) {
        PathPatternParser patternParser = new PathPatternParser();

        System.out.println("Checking public routes for path: " + path);

        return authProperties.getPublicRoutes().stream()
                .map(patternParser::parse)
                .anyMatch(pattern -> pattern.matches(PathContainer.parsePath(path)));
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> validateJwt(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (!request.getHeaders().containsKey("Authorization")) {
            return this.onError(exchange);
        }

        String authorization = request.getHeaders().getFirst("Authorization");
        assert authorization != null;  // já verificamos que o header existe
        String token = authorization.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(authProperties.getSecretKey().getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("userName", String.valueOf(claims.get("userName")))
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        }
        catch (Exception e) {
            System.out.println("JWT validation error: " + e.getMessage());
            return this.onError(exchange);
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();
        if (isPublic(path)) {
            System.out.println("Public route accessed: " + path);
            return chain.filter(exchange);
        } else {
            System.out.println("Protected route accessed: " + path);
            return validateJwt(exchange, chain);
        }
    }
}
