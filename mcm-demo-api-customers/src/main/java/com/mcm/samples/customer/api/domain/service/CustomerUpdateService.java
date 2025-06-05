package com.mcm.samples.customer.api.domain.service;

import java.time.LocalDateTime;

import com.mcm.samples.customer.api.application.repository.CustomerRepository;
import com.mcm.samples.customer.api.domain.cmd.UpdateCustomerCmd;
import com.mcm.samples.customer.api.domain.entity.Customer;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@Slf4j
public class CustomerUpdateService {

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private SecurityContext securityContext;

    @Transactional
    public Customer update(String customerId, UpdateCustomerCmd cmd) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        String username = securityContext.getCallerPrincipal().getName();
        log.info("User: {}", username);

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
        customer.getAuditInfo().setUpdatedAt(LocalDateTime.now());
        customer.getAuditInfo().setUpdatedBy(username);
        return customerRepository.update(customer);
    }

}
