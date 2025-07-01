package com.mcm.samples.customer.api.application.port.in.command;

import lombok.Data;

@Data
public class CustomerUpdateCommand {

    private String customerId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String updatedBy;

}
