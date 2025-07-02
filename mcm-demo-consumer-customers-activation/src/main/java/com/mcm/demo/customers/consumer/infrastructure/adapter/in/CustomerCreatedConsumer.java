package com.mcm.demo.customers.consumer.infrastructure.adapter.in;

import java.util.function.Consumer;

import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.mcm.demo.customers.consumer.domain.exception.CustomerActivationException;
import com.mcm.demo.customers.consumer.domain.port.in.ConsumerActivationUseCase;
import com.mcm.samples.customer.api.event.CustomerCreated;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class CustomerCreatedConsumer {

    @Autowired
    private ConsumerActivationUseCase consumerActivationService;

    //TODO fix this method to use CustomerCreated instead of GenericRecord
    @Bean
    public Consumer<Flux<Message<GenericRecord>>> customerCreated() {
        return flux -> flux
            .doOnNext(this::processMessage)
            .doOnError(Throwable::printStackTrace)
            .subscribe();
    }

    private void processMessage(Message<GenericRecord> message) {
        log.info("Processing customer activation << {}", message.getPayload());

        String customerId = message.getPayload().get("customerId").toString();
        String email = message.getPayload().get("email").toString();

        try {
            consumerActivationService.processActivation(customerId, email);
        }
        catch (Exception ex) {
            log.error("Error activating customer: {}", ex.getMessage(), ex);
            throw new CustomerActivationException(customerId, email, ex);
        }
    }

}
