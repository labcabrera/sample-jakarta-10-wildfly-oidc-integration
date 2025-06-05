package com.mcm.samples.customer.api.application.repository;

import java.util.Optional;

import com.mcm.samples.customer.api.domain.entity.Customer;
import com.mcm.samples.customer.api.domain.entity.Page;

public interface CustomerRepository {

    Optional<Customer> findById(String id);

    Optional<Customer> findByEmail(String email);

    Page<Customer> find(String searchExpression, int page, int size);

    Customer save(Customer customer);

    Customer update(Customer customer);

    void delete(String id);
}
