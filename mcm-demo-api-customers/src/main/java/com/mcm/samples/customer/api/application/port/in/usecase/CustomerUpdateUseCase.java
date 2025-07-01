package com.mcm.samples.customer.api.application.port.in.usecase;

import com.mcm.samples.customer.api.application.port.in.command.CustomerUpdateCommand;
import com.mcm.samples.customer.api.domain.model.Customer;
import com.mcm.samples.customer.api.domain.model.CustomerStatus;

public interface CustomerUpdateUseCase {

    Customer update(CustomerUpdateCommand command);

    Customer updateStatus(String customerId, CustomerStatus status, String updatedBy);

}
