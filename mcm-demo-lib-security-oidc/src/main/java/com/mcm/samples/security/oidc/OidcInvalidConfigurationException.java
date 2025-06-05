package com.mcm.samples.security.oidc;

public class OidcInvalidConfigurationException extends RuntimeException {

    public OidcInvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public OidcInvalidConfigurationException(Throwable cause) {
        super(cause);
    }

}
