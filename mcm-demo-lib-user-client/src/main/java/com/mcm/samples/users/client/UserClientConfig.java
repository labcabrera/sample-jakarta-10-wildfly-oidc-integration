package com.mcm.samples.users.client;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserClientConfig {

    public String getUserApiUrl() {
        return readFromEnv("APP_USERS_API_URL");
    }

    private String readFromEnv(String key) {
        String value = System.getenv(key);
        if (value != null && !value.isBlank()) {
            return value;
        }
        throw new RuntimeException(String.format("Required environment variable not set: '%s'", key));
    }

}
