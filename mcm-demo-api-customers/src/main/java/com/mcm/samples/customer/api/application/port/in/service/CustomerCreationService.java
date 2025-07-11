package com.mcm.samples.customer.api.application.port.in.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.mcm.samples.customer.api.application.port.in.command.CreateCustomerCommand;
import com.mcm.samples.customer.api.application.port.in.usecase.CustomerCreateUseCase;
import com.mcm.samples.customer.api.application.port.out.CustomerEventPublisher;
import com.mcm.samples.customer.api.application.port.out.CustomerRepository;
import com.mcm.samples.customer.api.domain.exception.ConstraintValidationException;
import com.mcm.samples.customer.api.domain.model.AuditInfo;
import com.mcm.samples.customer.api.domain.model.Customer;
import com.mcm.samples.customer.api.domain.model.CustomerContactInfo;
import com.mcm.samples.customer.api.domain.model.CustomerStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class CustomerCreationService implements CustomerCreateUseCase {

    private final Validator validator;
    private final CustomerRepository customerRepository;
    private final CustomerEventPublisher customerEventPublisher;

    @Inject
    public CustomerCreationService(
        Validator validator,
        CustomerRepository customerRepository,
        CustomerEventPublisher customerEventPublisher) {
        this.validator = validator;
        this.customerRepository = customerRepository;
        this.customerEventPublisher = customerEventPublisher;
    }

    @Override
    public Customer create(CreateCustomerCommand command) {
        log.info("Creating customer with command: {}", command);
        Set<ConstraintViolation<CreateCustomerCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintValidationException("Invalid customer.", violations);
        }
        if (customerRepository.findByEmail(command.getEmail()).isPresent()) {
            throw new ConstraintValidationException("Email already exists: " + command.getEmail());
        }
        Customer customer = Customer.builder()
            .firstName(command.getFirstName())
            .lastName(command.getLastName())
            .contactInfo(CustomerContactInfo.builder()
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .build())
            .status(CustomerStatus.PENDING_ACTIVATION)
            .auditInfo(AuditInfo.builder()
                .createdAt(LocalDateTime.now())
                .createdBy(command.getCreatedBy())
                .build())
            .build();
        Customer created = customerRepository.save(customer);
        publishCustomerCreatedEvent(created);
        return created;
    }

    private void publishCustomerCreatedEvent(Customer customer) {
        CompletableFuture<Void> future = customerEventPublisher.publishCustomerCreated(customer);
        future.thenRun(() -> log.info("Event published successfully for customer: {}", customer.getId()))
            .exceptionally(ex -> {
                log.error("Failed to publish event for customer: {}", customer.getId(), ex);
                return null;
            });
    }

}
