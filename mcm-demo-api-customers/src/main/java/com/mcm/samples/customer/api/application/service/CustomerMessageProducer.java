package com.mcm.samples.customer.api.application.service;

public interface CustomerMessageProducer {

    void sendCreatedCustomerEvent(String userId, String email);

}
