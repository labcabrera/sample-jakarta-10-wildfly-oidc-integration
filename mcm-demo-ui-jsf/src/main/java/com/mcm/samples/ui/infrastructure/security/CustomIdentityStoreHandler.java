package com.mcm.samples.ui.infrastructure.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class CustomIdentityStoreHandler implements IdentityStoreHandler {

    //TODO
    @Override
    public CredentialValidationResult validate(Credential credential) {
        CustomCredential customCredential = (CustomCredential) credential;
        String accessToken = customCredential.getAccessToken();

        log.info("Access token: {}", accessToken);

        String principal = "demo-principal";
        Set<String> groups = new HashSet<>(Arrays.asList("admin", "user"));

        return new CredentialValidationResult(principal, groups);
    }

}
