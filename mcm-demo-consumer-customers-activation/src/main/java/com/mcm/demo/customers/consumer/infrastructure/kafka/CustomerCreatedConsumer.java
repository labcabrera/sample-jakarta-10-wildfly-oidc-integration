package com.mcm.demo.customers.consumer.infrastructure.kafka;

import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcm.demo.customers.consumer.domain.exception.CustomerActivationException;
import com.mcm.demo.customers.consumer.domain.service.ConsumerActivationService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class CustomerCreatedConsumer {

    @Autowired
    private ConsumerActivationService consumerActivationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public Consumer<Flux<Message<String>>> customerCreated() {
        return flux -> flux
            .doOnNext(this::processMessage)
            .doOnError(Throwable::printStackTrace)
            .subscribe();
    }

    @SuppressWarnings("unchecked")
    private void processMessage(Message<String> message) {
        log.info("Processing message: {}", message.getPayload());
        String customerId;
        String email;
        try {
            Map<String, String> payload = objectMapper.readValue(message.getPayload(), Map.class);
            customerId = payload.get("customerId");
            email = payload.get("email");
        }
        catch (Exception ex) {
            log.error("Error processing message: {}", ex.getMessage(), ex);
            throw new CustomerActivationException("Invalid message format: " + message.getPayload());

        }
        try {
            consumerActivationService.processActivation(customerId, email);
        }
        catch (Exception ex) {
            log.error("Error activating customer: {}", ex.getMessage(), ex);
            throw new CustomerActivationException(customerId, email, ex);
        }
    }

}
