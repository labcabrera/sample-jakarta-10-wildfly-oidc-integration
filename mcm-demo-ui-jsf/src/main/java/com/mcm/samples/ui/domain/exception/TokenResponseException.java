package com.mcm.samples.ui.domain.exception;

public class TokenResponseException extends RuntimeException {

    public TokenResponseException(String message) {
        super(message);
    }

    public TokenResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenResponseException(Throwable cause) {
        super(cause);
    }

}
