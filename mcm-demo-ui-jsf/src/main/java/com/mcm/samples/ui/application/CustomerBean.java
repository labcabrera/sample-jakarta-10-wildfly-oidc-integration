package com.mcm.samples.ui.application;

import com.mcm.demo.api.client.model.CustomerPage;
import com.mcm.samples.ui.infrastructure.client.CustomerServiceRestClient;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Named
@RequestScoped
@Slf4j
public class CustomerBean {

    @Inject
    private CustomerServiceRestClient customerService;

    @Getter
    private CustomerPage customersPage;

    @PostConstruct
    public void init() {
        log.info("Fetching customers");
        customersPage = customerService.find("", 0, 10);
        log.info("Found customers: {}", customersPage.getContent().size());
    }

    public String deleteCustomer() {
        String customerId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("customerId");
        log.info("Deleting customer << {}", customerId);
        customerService.deleteById(customerId);
        return "customers?faces-redirect=true";
    }

}
