package com.mcm.samples.customer.api.application.port.in.service;

import com.mcm.samples.customer.api.application.port.in.usecase.CustomerDeleteUseCase;
import com.mcm.samples.customer.api.application.port.out.CustomerRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CustomerDeleteService implements CustomerDeleteUseCase {

    private final CustomerRepository customerRepository;

    @Inject
    public CustomerDeleteService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void delete(String customerId) {
        if (customerId == null || customerId.isEmpty()) {
            throw new IllegalArgumentException("Customer ID must not be null or empty");
        }
        customerRepository.delete(customerId);
    }

}
