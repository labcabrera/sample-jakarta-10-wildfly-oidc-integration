package com.mcm.samples.ui.domain;

import com.mcm.demo.api.client.model.CreateCustomerCmd;
import com.mcm.demo.api.client.model.Customer;
import com.mcm.demo.api.client.model.CustomerPage;

public interface CustomerService {

    public Customer findById(String customerId);

    public CustomerPage find(String searchExpression, Integer page, Integer size);

    public Customer create(CreateCustomerCmd cmd);

    public void deleteById(String customerId);
}
