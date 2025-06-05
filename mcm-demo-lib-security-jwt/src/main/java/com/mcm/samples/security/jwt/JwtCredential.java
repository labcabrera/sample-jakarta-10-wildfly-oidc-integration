package com.mcm.samples.security.jwt;

import java.io.Serializable;

import jakarta.security.enterprise.credential.Credential;
import lombok.Getter;

public class JwtCredential implements Credential, Serializable {

    @Getter
    private String accessToken;

    public JwtCredential(String accessToken) {
        this.accessToken = accessToken;
    }

}
