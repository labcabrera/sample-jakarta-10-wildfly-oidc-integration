package com.mcm.samples.customer.api.application.port.in.service;

import java.time.LocalDateTime;

import com.mcm.samples.customer.api.application.port.in.command.CustomerUpdateCommand;
import com.mcm.samples.customer.api.application.port.in.usecase.CustomerUpdateUseCase;
import com.mcm.samples.customer.api.application.port.out.CustomerRepository;
import com.mcm.samples.customer.api.domain.exception.CustomerNotModifiedException;
import com.mcm.samples.customer.api.domain.model.Customer;
import com.mcm.samples.customer.api.domain.model.CustomerStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class CustomerUpdateService implements CustomerUpdateUseCase {

    private final CustomerRepository customerRepository;

    @Inject
    public CustomerUpdateService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer update(CustomerUpdateCommand command) {
        String customerId = command.getCustomerId();
        Customer customer = getCustomer(customerId);
        if (command.getFirstName() != null) {
            customer.setFirstName(command.getFirstName());
        }
        if (command.getLastName() != null) {
            customer.setLastName(command.getLastName());
        }
        if (command.getEmail() != null) {
            customer.getContactInfo().setEmail(command.getEmail());
        }
        if (command.getPhoneNumber() != null) {
            customer.getContactInfo().setPhoneNumber(command.getPhoneNumber());
        }
        customer.getAuditInfo().setUpdatedAt(LocalDateTime.now());
        customer.getAuditInfo().setUpdatedBy(command.getUpdatedBy());
        return customerRepository.update(customer);
    }

    @Override
    public Customer updateStatus(String customerId, CustomerStatus status, String updatedBy) {
        log.info("Updating customer {} status from {} to {}", customerId, status);
        Customer customer = getCustomer(customerId);
        if (customer.getStatus() == status) {
            throw new CustomerNotModifiedException(customerId);
        }
        customer.setStatus(status);
        customer.getAuditInfo().setUpdatedAt(LocalDateTime.now());
        customer.getAuditInfo().setUpdatedBy(updatedBy);
        return customerRepository.update(customer);
    }

    private Customer getCustomer(String customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotModifiedException(customerId));
    }

}
