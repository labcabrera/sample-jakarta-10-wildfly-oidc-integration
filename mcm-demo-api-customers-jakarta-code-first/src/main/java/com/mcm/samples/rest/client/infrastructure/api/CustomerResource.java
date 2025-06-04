package com.mcm.samples.rest.client.infrastructure.api;

import java.time.LocalDateTime;
import java.util.Optional;

import com.mcm.samples.rest.client.application.service.CustomerService;
import com.mcm.samples.rest.client.domain.cmd.CreateCustomerCmd;
import com.mcm.samples.rest.client.domain.cmd.UpdateCustomerCmd;
import com.mcm.samples.rest.client.domain.entity.ApiError;
import com.mcm.samples.rest.client.domain.entity.Customer;
import com.mcm.samples.rest.client.domain.entity.Page;
import com.mcm.samples.rest.client.domain.exception.ParseException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class CustomerResource implements CustomerResourceDefinition {

    @Inject
    private CustomerService customerService;

    @Context
    private HttpHeaders httpHeaders;

    @Override
    public Response findById(String id) {
        log.debug("Customers << search by id {}", id);
        Optional<Customer> customer = customerService.findById(id);
        return customer.isPresent() ? Response.ok().entity(Optional.ofNullable(customer)).build()
            : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response find(String searchExpression, Integer page, Integer size) {
        log.info("Customers << search by search expression {} ({}, {})", searchExpression, page, size);

        //TODO just for debugging purposes to check the Authorization header is present
        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        log.info("Authorization header: {}", authorizationHeader);

        try {
            Page<Customer> customerPage = customerService.find(searchExpression, page, size);
            return Response.ok(customerPage).build();
        }
        catch (ParseException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiError.builder()
                    .code("400")
                    .message("Invalid search expression: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build())
                .build();
        }
    }

    @Override
    public Response createConsumer(CreateCustomerCmd cmd) {
        log.info("Customers << create customer {}", cmd);
        try {
            Customer customer = customerService.create(cmd);
            return Response.status(Response.Status.CREATED).entity(customer).build();
        }
        catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiError.builder()
                    .code("500")
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build())
                .build();
        }
    }

    @Override
    public Response updateConsumer(String customerId, UpdateCustomerCmd cmd) {
        Customer customer = customerService.update(customerId, cmd);
        return Response.ok().entity(customer).build();
    }

    @Override
    public Response delete(String id) {
        customerService.delete(id);
        return Response.noContent().build();
    }

}