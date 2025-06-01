package com.mcm.samples.rest.client.domain.service;

import java.time.LocalDateTime;
import java.util.Set;

import com.mcm.demo.api.model.CreateCustomerCmd;
import com.mcm.demo.api.model.Customer;
import com.mcm.demo.api.model.CustomerContactInfo;
import com.mcm.demo.api.model.CustomerStatus;
import com.mcm.samples.rest.client.application.repository.CustomerRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class CustomerCreationService {

    @Inject
    private Validator validator;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private EmailVerificationService emailVerificationService;

    @Transactional
    public Customer create(CreateCustomerCmd cmd) {
        log.info("Creating customer with command: {}", cmd);
        Set<ConstraintViolation<CreateCustomerCmd>> violations = validator.validate(cmd);
        if (!violations.isEmpty()) {
            //TODO create a custom exception for validation errors
            throw new RuntimeException("Invalid customer. " + violations);
        }
        if (customerRepository.findByEmail(cmd.getEmail()).isPresent()) {
            //TODO create a custom exception for validation errors
            throw new RuntimeException("Email already exists: " + cmd.getEmail());
        }
        Customer customer = new Customer();
        customer.setFirstName(cmd.getFirstName());
        customer.setLastName(cmd.getLastName());
        customer.setContactInfo(new CustomerContactInfo());
        customer.getContactInfo().setEmail(cmd.getEmail());
        customer.getContactInfo().setPhoneNumber(cmd.getPhoneNumber());
        customer.setStatus(CustomerStatus.PENDING_ACTIVATION);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setEmailVerified(false);
        Customer inserted = customerRepository.save(customer);
        emailVerificationService.sendEmailVerification(inserted.getId(), inserted.getContactInfo().getEmail());
        return inserted;
    }

}
