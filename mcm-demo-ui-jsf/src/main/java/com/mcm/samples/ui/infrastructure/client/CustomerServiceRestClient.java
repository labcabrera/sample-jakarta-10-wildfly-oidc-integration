package com.mcm.samples.ui.infrastructure.client;

import com.mcm.samples.ui.client.generated.customers.api.CustomersApi;
import com.mcm.samples.ui.client.generated.customers.invoker.ApiClient;
import com.mcm.samples.ui.client.generated.customers.invoker.ApiException;
import com.mcm.samples.ui.client.generated.customers.model.CreateCustomerCmd;
import com.mcm.samples.ui.client.generated.customers.model.Customer;
import com.mcm.samples.ui.client.generated.customers.model.CustomerPage;
import com.mcm.samples.ui.domain.exception.InvalidConfigurationException;
import com.mcm.samples.ui.domain.service.CustomerService;
import com.mcm.samples.ui.infrastructure.config.AppConfig;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@Slf4j
public class CustomerServiceRestClient implements CustomerService {

    private CustomersApi customersApi;

    @Inject
    private AppConfig appConfig;

    @PostConstruct
    public void init() {
        String customersApiUrl = appConfig.customerApiUrl();
        try {
            log.info("Initializing CustomerService with base URI: {}", customersApiUrl);
            String accessToken = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("access_token");
            log.info("Access token: {}", accessToken);
            ApiClient apiClient = new ApiClient();
            apiClient.updateBaseUri(customersApiUrl);
            apiClient.setRequestInterceptor(e -> {
                log.debug("Request Interceptor: {}", e);
                e.header("Authorization", "Bearer " + accessToken);
            });
            customersApi = new CustomersApi(apiClient);
            log.info("CustomerService initialized successfully");
        }
        catch (Exception ex) {
            log.error("Error initializing CustomerService: {}", ex.getMessage(), ex);
            throw new InvalidConfigurationException("", ex);
        }
    }

    @Override
    public Customer findById(String customerId) {
        log.debug("Customer search by id << {}", customerId);
        try {
            return customersApi.findCustomerById(customerId);
        }
        catch (ApiException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public CustomerPage find(String searchExpression, Integer page, Integer size) {
        log.debug("Customer search << {} ({}, {})", searchExpression, page, size);
        try {
            return customersApi.findCustomers(searchExpression, page, size);
        }
        catch (ApiException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Customer create(CreateCustomerCmd cmd) {
        log.debug("Customer create << {}", cmd.getEmail());
        try {
            return customersApi.createCustomer(cmd);
        }
        catch (ApiException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteById(String customerId) {
        log.debug("Customer delete << {}", customerId);
        try {
            customersApi.deleteCustomer(customerId);
        }
        catch (ApiException ex) {
            throw new RuntimeException(ex);
        }
    }

}
