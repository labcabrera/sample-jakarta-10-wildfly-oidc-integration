package com.mcm.demo.customers.consumer.domain.port.in;

public interface ConsumerActivationUseCase {

    void processActivation(String customerId, String email);

}
