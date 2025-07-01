package com.mcm.samples.customer.api.domain.exception;

public class CustomerNotModifiedException extends RuntimeException {

    public CustomerNotModifiedException() {
        super("Customer not modified");
    }

    public CustomerNotModifiedException(String customerId) {
        super("Customer not modified: " + customerId);
    }

}
