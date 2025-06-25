package com.mcm.demo.customers.consumer.infrastructure.kafka;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcm.demo.customers.consumer.domain.ConsumerActivationService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomerCreatedConsumer {

    @Autowired
    private ConsumerActivationService consumerActivationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public Consumer<Message<String>> customerCreated() {
        return message -> {
            log.info("Received message: {}", message.getPayload());
            log.info("Message Headers: {}", message.getHeaders());
            updateCustomer(message.getPayload());
        };
    }

    @SuppressWarnings("unchecked")
    private void updateCustomer(String message) {
        log.info("Processing message: {}", message);
        Map<String, String> messageBody;
        try {
            messageBody = objectMapper.readValue(message, Map.class);
        }
        catch (IOException ex) {
            log.error(message, ex);
            return;
        }
        String customerId = messageBody.get("userId");
        String email = messageBody.get("email");
        consumerActivationService.processActivation(customerId, email);
    }
}
