package com.mcm.samples.customer.api.domain.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException() {
        super("Customer not found");
    }

    public CustomerNotFoundException(String customerId) {
        super("Customer not found: " + customerId);
    }

}
