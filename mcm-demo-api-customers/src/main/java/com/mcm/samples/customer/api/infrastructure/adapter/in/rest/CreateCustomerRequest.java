package com.mcm.samples.customer.api.infrastructure.adapter.in.rest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCustomerRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    private String phoneNumber;

}
