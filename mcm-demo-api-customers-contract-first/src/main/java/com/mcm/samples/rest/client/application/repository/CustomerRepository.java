package com.mcm.samples.rest.client.application.repository;

import java.util.Optional;

import com.mcm.demo.api.model.Customer;
import com.mcm.demo.api.model.CustomerPage;

public interface CustomerRepository {

    Optional<Customer> findById(String id);

    Optional<Customer> findByEmail(String email);

    CustomerPage find(String searchExpression, int page, int size);

    Customer save(Customer customer);

    Customer update(Customer customer);

    void delete(String id);
}
