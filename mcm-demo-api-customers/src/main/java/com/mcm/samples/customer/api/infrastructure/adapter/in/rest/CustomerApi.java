package com.mcm.samples.customer.api.infrastructure.adapter.in.rest;

import java.util.Optional;

import org.modelmapper.ModelMapper;

import com.mcm.samples.customer.api.application.port.in.command.CreateCustomerCommand;
import com.mcm.samples.customer.api.application.port.in.command.CustomerUpdateCommand;
import com.mcm.samples.customer.api.application.port.in.usecase.CustomerCreateUseCase;
import com.mcm.samples.customer.api.application.port.in.usecase.CustomerDeleteUseCase;
import com.mcm.samples.customer.api.application.port.in.usecase.CustomerFindUseCase;
import com.mcm.samples.customer.api.application.port.in.usecase.CustomerUpdateUseCase;
import com.mcm.samples.customer.api.domain.model.Customer;
import com.mcm.samples.customer.api.domain.model.CustomerStatus;
import com.mcm.samples.customer.api.domain.model.Page;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@Slf4j
public class CustomerApi implements CustomerApiDefinition {

    @Inject
    private CustomerFindUseCase customerFindUseCase;

    @Inject
    private CustomerCreateUseCase customerCreateUseCase;

    @Inject
    private CustomerUpdateUseCase customerUpdateUseCase;

    @Inject
    private CustomerDeleteUseCase customerDeleteUseCase;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private ModelMapper modelMapper;

    @Override
    public Response findById(String id) {
        log.info("Customers << search by id {}", id);
        checkUserRole("customer-viewer", "User is not authorized to view customers.");
        Optional<Customer> customer = customerFindUseCase.findById(id);
        return customer.isPresent() ? Response.ok().entity(Optional.ofNullable(customer)).build()
            : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response find(String searchExpression, Integer page, Integer size) {
        log.info("Customers << search by search expression {} ({}, {})", searchExpression, page, size);
        checkUserRole("customer-viewer", "User is not authorized to view customers.");
        Page<Customer> customerPage = customerFindUseCase.find(searchExpression, page, size);
        return Response.ok(customerPage).build();
    }

    @Override
    public Response createConsumer(CustomerCreateRequest request) {
        log.info("Customers << create customer {}", request);
        checkUserRole("customer-manager", "User is not authorized to create customers.");
        String username = securityContext.getCallerPrincipal().getName();
        CreateCustomerCommand command = modelMapper.map(request, CreateCustomerCommand.class);
        command.setCreatedBy(username);
        Customer customer = customerCreateUseCase.create(command);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @Override
    public Response updateConsumer(String customerId, CustomerUpdateRequest request) {
        log.info("Customers << update customer {}", customerId);
        checkUserRole("customer-manager", "User is not authorized to modify customers.");
        CustomerUpdateCommand command = modelMapper.map(request, CustomerUpdateCommand.class);
        command.setCustomerId(customerId);
        command.setUpdatedBy(securityContext.getCallerPrincipal().getName());
        Customer customer = customerUpdateUseCase.update(command);
        return Response.ok().entity(customer).build();
    }

    @Override
    public Response updateConsumerStatus(String customerId, CustomerStatus status) {
        log.info("Customers << update customer status {} : {}", customerId, status);
        checkUserRole("customer-manager", "User is not authorized to update customer status.");
        String username = securityContext.getCallerPrincipal().getName();
        Customer customer = customerUpdateUseCase.updateStatus(customerId, status, username);
        return Response.ok().entity(customer).build();
    }

    @Override
    public Response delete(String customerId) {
        log.info("Customers << delete customer {}", customerId);
        checkUserRole("customer-manager", "User is not authorized to delete customers.");
        customerDeleteUseCase.delete(customerId);
        return Response.noContent().build();
    }

    private void checkUserRole(String role, String message) {
        if (!securityContext.isCallerInRole(role)) {
            throw new ForbiddenException(message);
        }
    }

}