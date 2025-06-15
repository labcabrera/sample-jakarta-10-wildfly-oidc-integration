package com.mcm.samples.security.jwt;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtConfig {

    public String jwtUri() {
        return readFromEnv("APP_JWK_URL");
    }

    private String readFromEnv(String key) {
        String value = System.getenv(key);
        if (value != null && !value.isBlank()) {
            return value;
        }
        throw new RuntimeException(String.format("Required environment variable not set: '%s'", key));
    }

}
