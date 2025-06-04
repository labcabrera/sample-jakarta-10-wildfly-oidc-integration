package com.mcm.samples.ui.infrastructure.security;

import jakarta.security.enterprise.credential.Credential;
import lombok.Getter;

public class CustomCredential implements Credential {

    @Getter
    private String accessToken;

    public CustomCredential(String accessToken) {
        this.accessToken = accessToken;
    }

}
