package com.mcm.samples.customer.api.application.port.in;

import java.util.Optional;

import com.mcm.samples.customer.api.domain.cmd.CreateCustomerCmd;
import com.mcm.samples.customer.api.domain.cmd.UpdateCustomerCmd;
import com.mcm.samples.customer.api.domain.entity.Customer;
import com.mcm.samples.customer.api.domain.entity.CustomerStatus;
import com.mcm.samples.customer.api.domain.entity.Page;

//TODO implement
public interface CustomerUseCase {

    Optional<Customer> findById(String customerId);

    Page<Customer> find(String searchExpression, int page, int size);

    Customer create(CreateCustomerCmd cmd);

    Customer update(String customerId, UpdateCustomerCmd cmd);

    Customer updateStatus(String customerId, CustomerStatus status);

    void delete(String customerId);

}
