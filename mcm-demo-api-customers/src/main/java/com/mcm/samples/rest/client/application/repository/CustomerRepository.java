package com.mcm.samples.rest.client.application.repository;

import java.util.Optional;

import com.mcm.samples.rest.client.domain.entity.Customer;
import com.mcm.samples.rest.client.domain.entity.Page;

public interface CustomerRepository {

    Optional<Customer> findById(String id);

    Optional<Customer> findByEmail(String email);

    Page<Customer> find(String searchExpression, int page, int size);

    Customer save(Customer customer);

    Customer update(Customer customer);

    void delete(String id);
}
