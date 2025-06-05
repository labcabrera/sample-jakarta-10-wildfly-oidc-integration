package com.mcm.samples.rest.client.infrastructure.security;

import java.io.Serializable;

import jakarta.security.enterprise.credential.Credential;
import lombok.Getter;

public class CustomCredential implements Credential, Serializable {

    @Getter
    private String accessToken;

    public CustomCredential(String accessToken) {
        this.accessToken = accessToken;
    }

}
