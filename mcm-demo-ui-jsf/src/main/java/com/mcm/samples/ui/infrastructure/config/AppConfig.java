package com.mcm.samples.ui.infrastructure.config;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppConfig {

    public String authorizationServerUrl() {
        return readFromEnv("APP_AUTH_SERVER_URL");
    }

    public String tokenUrl() {
        return readFromEnv("APP_TOKEN_URL");
    }

    public String jwkUrl() {
        return readFromEnv("APP_JWK_URL");
    }

    public String customerApiUrl() {
        return readFromEnv("APP_CUSTOMER_API_URL");
    }

    private String readFromEnv(String key) {
        String value = System.getenv(key);
        if (value != null && !value.isBlank()) {
            return value;
        }
        throw new RuntimeException(String.format("Required environment variable not set: '%s'", key));
    }

}
