package com.mcm.samples.customer.api.application.service;

import java.util.Optional;

import com.mcm.samples.customer.api.application.port.in.CreateCustomerCommand;
import com.mcm.samples.customer.api.application.port.in.CustomerCreationService;
import com.mcm.samples.customer.api.application.port.in.CustomerUpdateService;
import com.mcm.samples.customer.api.application.port.in.UpdateCustomerCommand;
import com.mcm.samples.customer.api.application.port.out.CustomerEventPublisher;
import com.mcm.samples.customer.api.application.port.out.CustomerRepository;
import com.mcm.samples.customer.api.domain.model.Customer;
import com.mcm.samples.customer.api.domain.model.CustomerStatus;
import com.mcm.samples.customer.api.domain.model.Page;

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
    private CustomerEventPublisher customerMessageProducer;

    public Optional<Customer> findById(String id) {
        return customerRepository.findById(id);
    }

    public Page<Customer> find(String searchExpression, int page, int size) {
        return customerRepository.find(searchExpression, page, size);
    }

    public Customer create(CreateCustomerCommand cmd) {
        Customer customer = customerCreationService.create(cmd);
        customerMessageProducer.publishCustomerCreated(customer);
        return customer;
    }

    public Customer update(String customerId, UpdateCustomerCommand cmd) {
        return customerUpdateService.update(customerId, cmd);
    }

    public Customer updateStatus(String customerId, CustomerStatus status) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        customer.setStatus(status);
        return customerRepository.update(customer);
    }

    public void delete(String customerId) {
        customerRepository.delete(customerId);
    }

}
