package com.mcm.demo.customers.consumer.infrastructure.client;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mcm.demo.customers.consumer.domain.ConsumerActivationService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConsumerActivationServiceApiClient implements ConsumerActivationService {

    @Autowired
    private TokenService tokenService;

    @Value("${api.customers.base-url}")
    private String customerApiBase;

    private RestTemplate restTemplate;

    public ConsumerActivationServiceApiClient() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void processActivation(String customerId, String email) {
        try {
            log.info("Processing customer activation << {} ({})", customerId, email);

            //TODO demo purpose only, use a real status check
            String status = email.contains("error") ? "inactive" : "active";

            String accessToken = tokenService.getAccessToken();
            log.info("Obtained access token {} ", accessToken);
            String url = customerApiBase + "/customers/" + customerId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            Map<String, String> requestBody = new LinkedHashMap<>();
            //TODO not implemented yet in the api
            // requestBody.put("status", status);
            requestBody.put("email", email);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            Map result = restTemplate.patchForObject(url, entity, Map.class);
            log.info("Customer update result: {}", result);
        }
        catch (Exception ex) {
            log.error("Error processing customer activation: {}", ex.getMessage(), ex);
            //TODO handle error
        }
    }

}
