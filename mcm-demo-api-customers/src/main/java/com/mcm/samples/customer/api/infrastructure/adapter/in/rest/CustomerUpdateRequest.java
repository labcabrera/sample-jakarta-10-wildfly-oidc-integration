package com.mcm.samples.customer.api.infrastructure.adapter.in.rest;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerUpdateRequest {

    @NotNull
    @Schema(description = "First name of the customer", example = "John")
    private String firstName;

    @NotNull
    @Schema(description = "Last name of the customer", example = "Doe")
    private String lastName;

    @NotNull
    @Email
    @Schema(description = "Email address of the customer", example = "john.doe@somehost.com")
    private String email;

    @Schema(description = "Phone number of the customer", example = "666 77 88 99")
    private String phoneNumber;

}
