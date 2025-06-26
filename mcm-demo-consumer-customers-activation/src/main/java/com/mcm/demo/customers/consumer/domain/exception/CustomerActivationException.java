package com.mcm.demo.customers.consumer.domain.exception;

public class CustomerActivationException extends RuntimeException {

    public CustomerActivationException(String customerId, String email, Throwable cause) {
        super(String.format("Error activating customer with ID: %s and email: %s", customerId, email), cause);
    }

    public CustomerActivationException(String message) {
        super(message);
    }

}
