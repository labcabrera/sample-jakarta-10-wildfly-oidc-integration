package com.mcm.samples.rest.client.infrastructure.security;

import java.util.Set;

import com.mcm.samples.rest.client.infrastructure.config.AppConfig;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.CallerPrincipal;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class CustomAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    private AppConfig appConfig;

    @Inject
    private CustomIdentityStoreHandler identityStoreHandler;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context)
        throws AuthenticationException {

        if (!appConfig.isEnabledSecurity()) {
            log.warn("Security is disabled, allowing unauthenticated access");
            return context.doNothing();
        }

        String header = request.getHeader("Authorization");
        log.info("Auhorization header: {}", header);

        if (header == null || !header.startsWith("Bearer ")) {
            log.warn("Authorization header is missing or does not start with 'Bearer '");
            return context.responseUnauthorized();
        }

        String token = header.substring("Bearer ".length()).trim();
        log.info("Received token: {}", token);

        CustomCredential customCredential = new CustomCredential(token);
        CredentialValidationResult validationResult = identityStoreHandler.validate(customCredential);
        CallerPrincipal principal = validationResult.getCallerPrincipal();
        Set<String> groups = validationResult.getCallerGroups();
        return context.notifyContainerAboutLogin(principal, groups);
    }

}