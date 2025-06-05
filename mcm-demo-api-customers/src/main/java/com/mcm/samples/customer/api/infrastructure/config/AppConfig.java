package com.mcm.samples.customer.api.infrastructure.config;

import java.io.InputStream;
import java.util.Properties;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppConfig {

    private final Properties config = new Properties();

    public AppConfig() {
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            config.load(in);
        }
        catch (Exception ex) {
            throw new RuntimeException("Error reading config.properties", ex);
        }
    }

    public boolean isEnabledSecurity() {
        String enabledSecurity = System.getenv("ENABLED_SECURITY");
        if (enabledSecurity != null) {
            return Boolean.parseBoolean(enabledSecurity);
        }
        return true;
    }

    public String jwtUri() {
        String jwtUri = System.getenv("JWK_URI");
        if (jwtUri != null && !jwtUri.isEmpty()) {
            return jwtUri;
        }
        jwtUri = config.getProperty("jwk.uri");
        if (jwtUri == null || jwtUri.isEmpty()) {
            throw new RuntimeException("JWK URI is not configured");
        }
        return jwtUri;
    }

}
