package com.mcm.samples.customer.api.domain.exception;

import java.util.Set;

import com.mcm.samples.customer.api.application.port.in.CreateCustomerCommand;

import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConstraintValidationException extends RuntimeException {

    private Set<ConstraintViolation<CreateCustomerCommand>> violations;

    public ConstraintValidationException(String message) {
        super(message);
    }

    public ConstraintValidationException(String message, Set<ConstraintViolation<CreateCustomerCommand>> violations) {
        //TODO
        super(message + ". " + violations.toString());
        this.violations = violations;
    }

}
