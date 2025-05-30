package com.mcm.samples.ui.application;

import com.mcm.demo.api.client.model.CreateCustomerCmd;
import com.mcm.samples.ui.infrastructure.client.CustomerServiceRestClient;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Named
@RequestScoped
@Slf4j
public class CustomerCreateBean {

    @Inject
    private CustomerServiceRestClient customerService;

    @Getter
    private CreateCustomerCmd createCustomerCmd = new CreateCustomerCmd();

    public String createCustomer() {
        log.info("Creating customer: {}", createCustomerCmd);
        customerService.create(createCustomerCmd);
        return "customers?faces-redirect=true";
    }

}
