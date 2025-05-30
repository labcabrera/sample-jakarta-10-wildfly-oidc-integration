package com.mcm.samples.rest.client.domain.service;

import java.time.LocalDateTime;
import java.util.Set;

import com.mcm.samples.rest.client.application.repository.CustomerRepository;
import com.mcm.samples.rest.client.domain.cmd.CreateCustomerCmd;
import com.mcm.samples.rest.client.domain.entity.Customer;
import com.mcm.samples.rest.client.domain.entity.CustomerContactInfo;
import com.mcm.samples.rest.client.domain.entity.CustomerStatus;
import com.mcm.samples.rest.client.domain.exception.ConstraintValidationException;

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
            throw new ConstraintValidationException("Invalid customer.", violations);
        }
        if (customerRepository.findByEmail(cmd.getEmail()).isPresent()) {
            throw new ConstraintValidationException("Email already exists: " + cmd.getEmail());
        }
        Customer customer = Customer.builder()
            .firstName(cmd.getFirstName())
            .lastName(cmd.getLastName())
            .contactInfo(CustomerContactInfo.builder()
                .email(cmd.getEmail())
                .phoneNumber(cmd.getPhoneNumber())
                .build())
            .status(CustomerStatus.PENDING_ACTIVATION)
            .emailVerified(false)
            .createdAt(LocalDateTime.now())
            .build();
        Customer inserted = customerRepository.save(customer);
        emailVerificationService.sendEmailVerification(inserted.getId(), inserted.getContactInfo().getEmail());
        return inserted;
    }

}
