package com.mcm.samples.security.oidc;

public class OidcTokenResponseException extends RuntimeException {

    public OidcTokenResponseException(String message) {
        super(message);
    }

    public OidcTokenResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public OidcTokenResponseException(Throwable cause) {
        super(cause);
    }

}
