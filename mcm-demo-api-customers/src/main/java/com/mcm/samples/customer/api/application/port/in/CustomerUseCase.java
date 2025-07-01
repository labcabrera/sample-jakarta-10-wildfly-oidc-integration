package com.mcm.samples.customer.api.application.port.in;

import java.util.Optional;

import com.mcm.samples.customer.api.domain.model.Customer;
import com.mcm.samples.customer.api.domain.model.CustomerStatus;
import com.mcm.samples.customer.api.domain.model.Page;

//TODO implement
public interface CustomerUseCase {

    Optional<Customer> findById(String customerId);

    Page<Customer> find(String searchExpression, int page, int size);

    Customer create(CreateCustomerCommand cmd);

    Customer update(String customerId, UpdateCustomerCommand cmd);

    Customer updateStatus(String customerId, CustomerStatus status);

    void delete(String customerId);

}
