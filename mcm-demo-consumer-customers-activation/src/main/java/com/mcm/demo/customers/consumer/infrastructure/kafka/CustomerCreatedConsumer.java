package com.mcm.demo.customers.consumer.infrastructure.kafka;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomerCreatedConsumer {

    @Bean
    public Consumer<Message<String>> customerCreated() {
        return message -> {
            log.info("Received message: {}", message.getPayload());
            //TODO
        };
    }
}
