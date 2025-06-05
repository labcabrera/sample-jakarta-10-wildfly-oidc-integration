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

    public String clientId() {
        return getProperty("CLIENT_ID", "client.id", null);
    }

    public String clientSecret() {
        return getProperty("CLIENT_SECRET", "client.secret", null);
    }

    public String authorizationServerUrl() {
        return getProperty("AUTH_SERVER_URL", "auth.server.url", null);
    }

    public String tokenUrl() {
        return getProperty("TOKEN_URL", "token.endpoint", null);
    }

    public String logoutUrl() {
        return getProperty("LOGOUT_URL", "logout.url", null);
    }

    public String callbackUrl() {
        return getProperty("CALLBACK_URL", "callback.url", null);
    }

    public String loginRedirectUrl() {
        return getProperty("LOGIN_REDIRECT_URL", "login.redirect", null);
    }

    public String logoutRedirectUrl() {
        return getProperty("LOGOUT_REDIRECT_URL", "logout.redirect", null);
    }

    public String jwkUri() {
        return getProperty("JWK_URL", "jwk.url", null);
    }

    public String scope() {
        return getProperty("SCOPE", "scope", "openid profile email");
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
