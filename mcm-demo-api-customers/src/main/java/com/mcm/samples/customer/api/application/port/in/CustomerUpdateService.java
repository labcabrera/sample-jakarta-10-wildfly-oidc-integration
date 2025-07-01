package com.mcm.samples.customer.api.application.port.in;

import java.time.LocalDateTime;

import com.mcm.samples.customer.api.application.port.out.CustomerRepository;
import com.mcm.samples.customer.api.domain.model.Customer;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@Slf4j
public class CustomerUpdateService {

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private SecurityContext securityContext;

    public Customer update(String customerId, UpdateCustomerCommand cmd) {
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
