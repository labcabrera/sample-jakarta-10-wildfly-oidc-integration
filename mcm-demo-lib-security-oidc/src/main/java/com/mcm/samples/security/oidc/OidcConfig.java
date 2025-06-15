package com.mcm.samples.security.oidc;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OidcConfig {

    public String clientId() {
        return readFromEnv("APP_CLIENT_ID");
    }

    public String clientSecret() {
        return readFromEnv("APP_CLIENT_SECRET");
    }

    public String authorizationServerUrl() {
        return readFromEnv("APP_AUTH_SERVER_URL");
    }

    public String tokenUrl() {
        return readFromEnv("APP_TOKEN_URL");
    }

    public String logoutUrl() {
        return readFromEnv("APP_LOGOUT_URL");
    }

    public String callbackUrl() {
        return readFromEnv("APP_CALLBACK_URL");
    }

    public String loginRedirectUrl() {
        return readFromEnv("APP_LOGIN_REDIRECT_URL");
    }

    public String logoutRedirectUrl() {
        return readFromEnv("APP_LOGOUT_REDIRECT_URL");
    }

    public String jwkUri() {
        return readFromEnv("APP_JWK_URL");
    }

    public String scope() {
        return readFromEnv("APP_OIDC_SCOPE");
    }

    private String readFromEnv(String key) {
        String value = System.getenv(key);
        if (value != null && !value.isBlank()) {
            return value;
        }
        throw new RuntimeException(String.format("Required environment variable not set: '%s'", key));
    }

}
