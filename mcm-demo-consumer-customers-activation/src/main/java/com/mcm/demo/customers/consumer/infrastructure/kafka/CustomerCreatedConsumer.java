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
        try {
            log.info("Processing message: {}", message.getPayload());
            Map<String, String> payload = objectMapper.readValue(message.getPayload(), Map.class);
            String customerId = payload.get("userId");
            String email = payload.get("email");
            consumerActivationService.processActivation(customerId, email);
        }
        catch (IOException ex) {
            //TODO handle error properly
            log.error("Error processing message: {}", ex.getMessage(), ex);
        }
    }

}
