package com.mcm.samples.rest.client.infrastructure.api;

import com.mcm.demo.api.ApiApi;
import com.mcm.demo.api.model.CreateCustomerCmd;
import com.mcm.demo.api.model.Customer;
import com.mcm.demo.api.model.CustomerPage;
import com.mcm.demo.api.model.UpdateCustomerCmd;
import com.mcm.samples.rest.client.application.repository.CustomerRepository;
import com.mcm.samples.rest.client.domain.service.CustomerCreationService;
import com.mcm.samples.rest.client.domain.service.CustomerUpdateService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class CustomerApiImpl implements ApiApi {

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerCreationService customerCreationService;

    @Inject
    private CustomerUpdateService customerUpdateService;

    @Override
    public CustomerPage findCustomers(Integer page, String query, Integer size) {
        return customerRepository.find(query, page != null ? page : 0, size != null ? size : 10);
    }

    @Override
    public void deleteCustomer(String id) {
        customerRepository.delete(id);
    }

    @Override
    public Customer findCustomerById(String id) {
        return customerRepository.findById(id).get();
    }

    @Override
    public Customer updateCustomer(String id, @Valid @NotNull UpdateCustomerCmd updateCustomerCmd) {
        return customerUpdateService.update(id, updateCustomerCmd);
    }

    @Override
    public Customer createCustomer(@Valid @NotNull CreateCustomerCmd createCustomerCmd) {
        return customerCreationService.create(createCustomerCmd);
    }

}
