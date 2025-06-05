package com.mcm.samples.security.jwt;

import java.io.InputStream;
import java.util.Properties;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtConfig {

    private final Properties config = new Properties();

    public JwtConfig() {
        try (InputStream in = JwtConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            config.load(in);
        }
        catch (Exception ex) {
            throw new RuntimeException("Error reading config.properties", ex);
        }
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
