package com.mcm.samples.security.oidc;

import java.io.InputStream;
import java.util.Properties;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OidcConfig {

    private final Properties config = new Properties();

    public OidcConfig() {
        try (InputStream in = OidcConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            config.load(in);
        }
        catch (Exception ex) {
            throw new RuntimeException("Error reading config.properties", ex);
        }
    }

    public String authorizationServerUrl() {
        return getProperty("AUTH_SERVER_URL", "auth.server.url", null);
    }

    public String tokenUrl() {
        return getProperty("TOKEN_URL", "token.endpoint", null);
    }

    public String jwkUri() {
        return getProperty("JWK_URL", "jwk.url", null);
    }

    public String customerApiUrl() {
        return getProperty("CUSTOMER_API_URL", "customers.api.url", null);
    }

    private String getProperty(String env, String key, String defaultValue) {
        String value = System.getenv(env);
        if (value != null) {
            return value;
        }
        value = config.getProperty(key);
        if (value != null) {
            return value;
        }
        if (defaultValue == null) {
            throw new RuntimeException("Property '" + key + "' is not configured and no default value provided");
        }
        return defaultValue;
    }

}
