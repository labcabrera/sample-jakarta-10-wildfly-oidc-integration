package com.mcm.demo.customers.consumer.infrastructure.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.mcm.demo.customers.consumer.domain.ConsumerActivationService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebClientConsumerActivationService implements ConsumerActivationService {

    @Autowired
    private WebClient webClient;

    @Value("${app.api-client.customers.base-url}")
    private String customerApiBaseUrl;

    @Override
    public void processActivation(String customerId, String email) {
        try {
            String status = resolveStatus(email);
            log.info("Updating customer {} status to {} over {}", customerId, status, customerApiBaseUrl);
            String result = webClient.patch()
                .uri(customerApiBaseUrl + "/customers/{customerId}/status/{status}", customerId, status)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            log.info("Patch result:  {}", result);
        }
        catch (Exception ex) {
            log.error("Error updating customer status: {}", ex.getMessage(), ex);
        }
    }

    private String resolveStatus(String email) {
        // Mock business logic
        return email.contains("error") ? "inactive" : "active";
    }

}
