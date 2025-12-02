package br.gov.sp.fatec.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "application.authentication")
public class AuthProperties {
    private String secretKey;
    private List<String> publicRoutes;

    public AuthProperties() {
    }

    public AuthProperties(String secretKey, List<String> publicRoutes) {
        this.secretKey = secretKey;
        this.publicRoutes = publicRoutes;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public List<String> getPublicRoutes() {
        return publicRoutes;
    }

    public void setPublicRoutes(List<String> publicRoutes) {
        this.publicRoutes = publicRoutes;
    }
}
