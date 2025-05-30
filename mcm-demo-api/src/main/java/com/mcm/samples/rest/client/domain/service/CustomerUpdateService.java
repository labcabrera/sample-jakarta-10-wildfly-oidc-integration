package com.mcm.samples.rest.client.domain.service;

import java.time.LocalDateTime;

import com.mcm.samples.rest.client.application.repository.CustomerRepository;
import com.mcm.samples.rest.client.domain.cmd.UpdateCustomerCmd;
import com.mcm.samples.rest.client.domain.entity.Customer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CustomerUpdateService {

    @Inject
    private CustomerRepository customerRepository;

    @Transactional
    public Customer update(String customerId, UpdateCustomerCmd cmd) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

        boolean isEmailChanged = cmd.getEmail() != null && !customer.getContactInfo().getEmail().equals(cmd.getEmail());

        if (cmd.getFirstName() != null) {
            customer.setFirstName(cmd.getFirstName());
        }
        if (cmd.getLastName() != null) {
            customer.setLastName(cmd.getLastName());
        }
        if (cmd.getEmail() != null) {
            customer.getContactInfo().setEmail(cmd.getEmail());
        }
        if (cmd.getPhoneNumber() != null) {
            customer.getContactInfo().setPhoneNumber(cmd.getPhoneNumber());
        }
        customer.setUpdatedAt(LocalDateTime.now());

        if (isEmailChanged) {
            customer.setEmailVerified(false);
        }

        return customerRepository.update(customer);
    }

}
