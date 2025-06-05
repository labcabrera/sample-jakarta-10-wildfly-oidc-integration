package com.mcm.samples.ui.domain.exception;

public class InvalidConfigurationException extends RuntimeException {

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

}
