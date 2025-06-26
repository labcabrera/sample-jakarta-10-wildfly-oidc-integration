package com.mcm.demo.customers.consumer.domain.service;

public interface ConsumerActivationService {

    void processActivation(String customerId, String email);

}
