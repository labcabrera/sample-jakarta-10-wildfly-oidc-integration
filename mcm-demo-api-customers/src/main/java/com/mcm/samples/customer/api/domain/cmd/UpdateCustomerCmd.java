package com.mcm.samples.customer.api.domain.cmd;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateCustomerCmd {

    @Schema(description = "New customer name", example = "John")
    private String firstName;

    @Schema(description = "New last name of the customer", example = "Doe")
    private String lastName;

    @Email
    @Schema(description = "New email address of the customer", example = "john.doe@somehost.com")
    private String email;

    @Schema(description = "New phone number of the customer", example = "666 77 88 99")
    private String phoneNumber;
}
