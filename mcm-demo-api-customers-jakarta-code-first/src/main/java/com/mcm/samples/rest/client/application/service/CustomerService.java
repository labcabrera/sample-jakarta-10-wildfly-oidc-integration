package com.mcm.samples.rest.client.application.service;

import java.util.Optional;

import com.mcm.samples.rest.client.application.repository.CustomerRepository;
import com.mcm.samples.rest.client.domain.cmd.CreateCustomerCmd;
import com.mcm.samples.rest.client.domain.cmd.UpdateCustomerCmd;
import com.mcm.samples.rest.client.domain.entity.Customer;
import com.mcm.samples.rest.client.domain.entity.Page;
import com.mcm.samples.rest.client.domain.service.CustomerCreationService;
import com.mcm.samples.rest.client.domain.service.CustomerUpdateService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CustomerService {

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerCreationService customerCreationService;

    @Inject
    private CustomerUpdateService customerUpdateService;

    public Optional<Customer> findById(String id) {
        return customerRepository.findById(id);
    }

    public Page<Customer> find(String searchExpression, int page, int size) {
        return customerRepository.find(searchExpression, page, size);
    }

    public Customer create(CreateCustomerCmd cmd) {
        return customerCreationService.create(cmd);
    }

    public Customer update(String customerId, UpdateCustomerCmd cmd) {
        return customerUpdateService.update(customerId, cmd);
    }

    @Transactional
    public void delete(String customerId) {
        customerRepository.delete(customerId);
    }

}
