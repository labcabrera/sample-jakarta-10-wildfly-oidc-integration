package com.mcm.demo.customers.consumer.domain;

public interface ConsumerActivationService {

    void processActivation(String customerId, String email);

}
