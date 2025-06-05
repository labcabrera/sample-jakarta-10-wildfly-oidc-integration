package com.mcm.samples.security.oidc;

import java.io.Serializable;

import jakarta.security.enterprise.credential.Credential;
import lombok.Getter;

public class OidcCredential implements Credential, Serializable {

    @Getter
    private String accessToken;

    public OidcCredential(String accessToken) {
        this.accessToken = accessToken;
    }

}
