package com.mcm.samples.customer.api.infrastructure.api;

import java.util.Optional;

import com.mcm.samples.customer.api.application.service.CustomerService;
import com.mcm.samples.customer.api.domain.cmd.CreateCustomerCmd;
import com.mcm.samples.customer.api.domain.cmd.UpdateCustomerCmd;
import com.mcm.samples.customer.api.domain.entity.Customer;
import com.mcm.samples.customer.api.domain.entity.Page;

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
    private CustomerService customerService;

    @Inject
    private SecurityContext securityContext;

    @Override
    public Response findById(String id) {
        log.debug("Customers << search by id {}", id);
        checkUserRole("customer-viewer", "User is not authorized to view customers.");
        Optional<Customer> customer = customerService.findById(id);
        return customer.isPresent() ? Response.ok().entity(Optional.ofNullable(customer)).build()
            : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response find(String searchExpression, Integer page, Integer size) {
        log.info("Customers << search by search expression {} ({}, {})", searchExpression, page, size);
        checkUserRole("customer-viewer", "User is not authorized to view customers.");
        Page<Customer> customerPage = customerService.find(searchExpression, page, size);
        return Response.ok(customerPage).build();
    }

    @Override
    public Response createConsumer(CreateCustomerCmd cmd) {
        log.info("Customers << create customer {}", cmd);
        checkUserRole("customer-manager", "User is not authorized to create customers.");
        Customer customer = customerService.create(cmd);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @Override
    public Response updateConsumer(String customerId, UpdateCustomerCmd cmd) {
        log.info("Customers << update customer {}", customerId);
        checkUserRole("customer-manager", "User is not authorized to modify customers.");
        Customer customer = customerService.update(customerId, cmd);
        return Response.ok().entity(customer).build();
    }

    @Override
    public Response delete(String customerId) {
        log.info("Customers << delete customer {}", customerId);
        checkUserRole("customer-manager", "User is not authorized to delete customers.");
        customerService.delete(customerId);
        return Response.noContent().build();
    }

    private void checkUserRole(String role, String message) {
        if (!securityContext.isCallerInRole(role)) {
            throw new ForbiddenException(message);
        }
    }

}