package com.mcm.samples.customer.api.application.service;

import java.util.Optional;

import com.mcm.samples.customer.api.application.repository.CustomerRepository;
import com.mcm.samples.customer.api.domain.cmd.CreateCustomerCmd;
import com.mcm.samples.customer.api.domain.cmd.UpdateCustomerCmd;
import com.mcm.samples.customer.api.domain.entity.Customer;
import com.mcm.samples.customer.api.domain.entity.Page;
import com.mcm.samples.customer.api.domain.service.CustomerCreationService;
import com.mcm.samples.customer.api.domain.service.CustomerUpdateService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CustomerService {

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerCreationService customerCreationService;

    @Inject
    private CustomerUpdateService customerUpdateService;

    @Inject
    private CustomerMessageProducer customerMessageProducer;

    public Optional<Customer> findById(String id) {
        return customerRepository.findById(id);
    }

    public Page<Customer> find(String searchExpression, int page, int size) {
        return customerRepository.find(searchExpression, page, size);
    }

    public Customer create(CreateCustomerCmd cmd) {
        Customer customer = customerCreationService.create(cmd);
        customerMessageProducer.sendCreatedCustomerEvent(customer.getId(), customer.getContactInfo().getEmail());
        return customer;
    }

    public Customer update(String customerId, UpdateCustomerCmd cmd) {
        return customerUpdateService.update(customerId, cmd);
    }

    public void delete(String customerId) {
        customerRepository.delete(customerId);
    }

}
