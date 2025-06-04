package com.mcm.samples.ui.infrastructure.client;

import com.mcm.demo.api.client.CustomersApi;
import com.mcm.demo.api.client.invoker.ApiClient;
import com.mcm.demo.api.client.invoker.ApiException;
import com.mcm.demo.api.client.model.CreateCustomerCmd;
import com.mcm.demo.api.client.model.Customer;
import com.mcm.demo.api.client.model.CustomerPage;
import com.mcm.samples.ui.domain.service.CustomerService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@Slf4j
public class CustomerServiceRestClient implements CustomerService {

    private CustomersApi customersApi;

    @PostConstruct
    public void init() {
        String host = System.getenv().getOrDefault("CUSTOMER_API_HOST", "127.0.0.1");
        int port = Integer.parseInt(System.getenv().getOrDefault("CUSTOMER_API_PORT", "8081"));
        String scheme = System.getenv().getOrDefault("CUSTOMER_API_SCHEME", "http");
        String basePath = System.getenv().getOrDefault("CUSTOMER_API_BASE_PATH", "/demo-api");

        log.info("Initializing CustomerService");
        log.info("Customer API config - host: {}, port: {}, scheme: {}, basePath: {}", host, port, scheme, basePath);

        String accessToken = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("access_token");
        log.info("Access token: {}", accessToken);

        customersApi = new CustomersApi();
        ApiClient apiClient = new ApiClient();
        apiClient.setHost(host);
        apiClient.setPort(port);
        apiClient.setScheme(scheme);
        apiClient.setBasePath(basePath);
        // apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        customersApi = new CustomersApi(apiClient);

        log.info("CustomerService initialized successfully");
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
