package com.mcm.samples.customer.api.application.port.in.command;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCustomerCommand {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    private String phoneNumber;

    @NotNull
    private String createdBy;

}
