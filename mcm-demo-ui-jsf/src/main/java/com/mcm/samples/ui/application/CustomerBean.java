package com.mcm.samples.ui.application;

import com.mcm.samples.ui.client.generated.customers.model.CustomerPage;
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

    @Getter
    private String errorMessage;

    @PostConstruct
    public void init() {
        log.info("Fetching customers");
        try {
            customersPage = customerService.find("", 0, 10);
            log.info("Found customers: {}", customersPage.getContent().size());
        }
        catch (Exception ex) {
            log.error("Error fetching customers", ex);
            errorMessage = ex.getMessage();
        }
    }

    public String deleteCustomer() {
        try {
            String customerId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("customerId");
            log.info("Deleting customer << {}", customerId);
            customerService.deleteById(customerId);
            return "customers?faces-redirect=true";
        }
        catch (Exception ex) {
            log.error("Error deleting customer", ex);
            errorMessage = ex.getMessage();
            return null;
        }
    }

}
