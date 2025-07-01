package com.mcm.samples.customer.api.application.port.in.service;

import java.util.Optional;

import com.mcm.samples.customer.api.application.port.in.usecase.CustomerFindUseCase;
import com.mcm.samples.customer.api.application.port.out.CustomerRepository;
import com.mcm.samples.customer.api.domain.model.Customer;
import com.mcm.samples.customer.api.domain.model.Page;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CustomerFindService implements CustomerFindUseCase {

    private final CustomerRepository customerRepository;

    @Inject
    public CustomerFindService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> findById(String id) {
        return customerRepository.findById(id);
    }

    public Page<Customer> find(String searchExpression, int page, int size) {
        return customerRepository.find(searchExpression, page, size);
    }

}
