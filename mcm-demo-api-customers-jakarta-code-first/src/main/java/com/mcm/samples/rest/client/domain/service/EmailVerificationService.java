package com.mcm.samples.rest.client.domain.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class EmailVerificationService {

    public void sendEmailVerification(String customerId, String email) {
        log.info("Sending email verification for customerId: {}, email: {}", customerId, email);
        //TODO integrar con una cola de mensajeria
    }

}
