package com.mcm.samples.customer.api.application.port.in.usecase;

import java.util.Optional;

import com.mcm.samples.customer.api.domain.model.Customer;
import com.mcm.samples.customer.api.domain.model.Page;

public interface CustomerFindUseCase {

    Optional<Customer> findById(String customerId);

    Page<Customer> find(String searchExpression, int page, int size);

}
