package com.mcm.samples.customer.api.application.port.in.usecase;

import com.mcm.samples.customer.api.application.port.in.command.CreateCustomerCommand;
import com.mcm.samples.customer.api.domain.model.Customer;

public interface CustomerCreateUseCase {

    Customer create(CreateCustomerCommand command);

}
