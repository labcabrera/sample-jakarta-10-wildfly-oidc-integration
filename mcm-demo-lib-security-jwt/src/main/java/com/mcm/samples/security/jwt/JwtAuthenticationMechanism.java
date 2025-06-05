package com.mcm.samples.security.jwt;

import java.io.IOException;
import java.util.Set;

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
public class JwtAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    private JwtIdentityStoreHandler identityStoreHandler;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context)
        throws AuthenticationException {

        String header = request.getHeader("Authorization");
        log.info("Auhorization header: {}", header);

        if (header == null || header.isEmpty()) {
            log.warn("Authorization header is missing");
            writeError(response, "Authorization header is required.");
            return AuthenticationStatus.SEND_FAILURE;
        }
        else if (!header.startsWith("Bearer ")) {
            log.warn("Authorization header is missing or does not start with 'Bearer '");
            writeError(response, "Invalid Authorization header format, expected 'Bearer <token>'.");
            return AuthenticationStatus.SEND_FAILURE;
        }

        String token = header.substring("Bearer ".length()).trim();
        log.info("Received token: {}", token);

        JwtCredential customCredential = new JwtCredential(token);
        CredentialValidationResult validationResult = identityStoreHandler.validate(customCredential);
        CallerPrincipal principal = validationResult.getCallerPrincipal();
        Set<String> groups = validationResult.getCallerGroups();
        log.info("Execution notifyContainerAboutLogin {} {}", principal.getName(), groups);
        return context.notifyContainerAboutLogin(principal, groups);
    }

    private void writeError(HttpServletResponse response, String message) {
        try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            String json = String.format("{\"code\":\"401\",\"message\":\"%s\"}", message);
            response.getWriter().write(json);
            response.getWriter().flush();
        }
        catch (IOException ex) {
            throw new RuntimeException("Error writing error", ex);
        }
    }

}