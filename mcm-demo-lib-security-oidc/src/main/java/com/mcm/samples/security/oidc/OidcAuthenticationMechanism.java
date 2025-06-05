package com.mcm.samples.security.oidc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.CallerPrincipal;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class OidcAuthenticationMechanism implements HttpAuthenticationMechanism {

    private static final Properties config = new Properties();

    static {
        try (InputStream in = OidcAuthenticationMechanism.class.getClassLoader().getResourceAsStream("config.properties")) {
            config.load(in);
        }
        catch (Exception ex) {
            throw new OidcInvalidConfigurationException(ex);
        }
    }

    // private static final String CLIENT_ID = config.getProperty("client.id");
    // private static final String CLIENT_SECRET = config.getProperty("client.secret");
    //private static final String REDIRECT_URI = config.getProperty("redirect.uri");
    // private static final String BASE_URL = config.getProperty("base.url");

    private static final String PARAM_CODE = "code";
    private static final String SESSION_STATE = "OIDC_STATE";

    @Inject
    private OidcIdentityStoreHandler identityStoreHandler;

    @Inject
    private HttpClient httpClient;

    @Inject
    private Jsonb jsonb;

    @Inject
    private OidcConfig appConfig;

    @Override
    @SuppressWarnings("unchecked")
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context)
        throws AuthenticationException {

        log.info("------------------------------------------------------------");
        log.info("Validating request using OIDC authentication");
        log.info("  URL                     : {}", request.getRequestURI());
        log.info("  Session ID              : {}", request.getSession() != null ? request.getSession().getId() : null);
        log.info("  Session Principal       : {}", request.getSession() != null ? request.getSession().getAttribute("principal") : null);
        log.info("  Context Principal       : {}", context.getCallerPrincipal());
        log.info("  isProtected             : {}", context.isProtected());
        log.info("  isAuthenticationRequest : {}", context.isAuthenticationRequest());
        log.info("------------------------------------------------------------");

        if (request.getSession().getAttribute("principal") != null) {
            log.info("  Principal in session. Do nothing");
            CallerPrincipal principal = (CallerPrincipal) request.getSession().getAttribute("principal");
            Set<String> groups = (Set<String>) request.getSession().getAttribute("groups");
            return context.notifyContainerAboutLogin(principal, groups);
        }

        String path = request.getRequestURI().substring(request.getContextPath().length());
        String code = request.getParameter(PARAM_CODE);

        if (path.equals("/callback") && code != null) {
            log.info("Callback processing code {}", code);
            try {
                OidcTokenResponse tokenResponse = readTokenFromIdP(code);

                if (tokenResponse != null && tokenResponse.getId_token() != null) {
                    OidcCredential customCredentials = new OidcCredential(tokenResponse.getAccess_token());
                    CredentialValidationResult validationResult = identityStoreHandler.validate(customCredentials);
                    log.info("Credential validation result: {}", validationResult.getStatus());

                    if (validationResult.getStatus() == CredentialValidationResult.Status.VALID) {
                        CallerPrincipal principal = validationResult.getCallerPrincipal();
                        Set<String> groups = validationResult.getCallerGroups();
                        log.info("Authentication successful for user: {} with groups {}", principal.getName(), groups);

                        request.getSession().setAttribute("access_token", tokenResponse.getAccess_token());
                        request.getSession().setAttribute("id_token", tokenResponse.getId_token());
                        request.getSession().setAttribute("username", principal.getName());
                        request.getSession().setAttribute("principal", principal);
                        request.getSession().setAttribute("groups", groups);

                        response.sendRedirect(appConfig.loginRedirectUrl());
                        return context.notifyContainerAboutLogin(principal, groups);
                    }
                    else {
                        log.info("Unauthorized with validation result code: {}", validationResult.getStatus());
                        return context.responseUnauthorized();
                    }
                }
                else {
                    log.info("Unauthorized");
                    return context.responseUnauthorized();
                }
            }
            catch (Exception ex) {
                log.error("Callback code error", ex);
                throw new AuthenticationException("Error al procesar callback OIDC", ex);
            }
        }

        if (context.getCallerPrincipal() != null) {
            log.info("User already authenticated: {}", context.getCallerPrincipal().getName());
            return context.doNothing();
        }

        if (context.isAuthenticationRequest()) {
            log.info("Is an authentication request, but no code provided: {}", path);
            return context.doNothing();
        }

        // If we reach here, we need to redirect to the OIDC provider for authentication
        return redirectToIdp(request, response, context);
    }

    private OidcTokenResponse readTokenFromIdP(String code) throws IOException, InterruptedException {
        log.info("Reading token from Keycloak for code {}", code);

        StringBuilder form = new StringBuilder();
        form.append("grant_type=authorization_code");
        form.append("&code=").append(code);
        form.append("&redirect_uri=").append(appConfig.callbackUrl());
        form.append("&client_id=").append(appConfig.clientId());
        form.append("&client_secret=").append(appConfig.clientSecret());

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(appConfig.tokenUrl()))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(form.toString()))
            .build();

        HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            String body = resp.body();
            log.info("-- Token endpoint response start --/n{}/n-- Token endpoint response end --", body);
            return jsonb.fromJson(body, OidcTokenResponse.class);
        }
        throw new OidcTokenResponseException(String.format("Error reading token: %s. Message: %s", resp.statusCode(), resp.body()));
    }

    private AuthenticationStatus redirectToIdp(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context)
        throws AuthenticationException {
        String state = UUID.randomUUID().toString();
        request.getSession().setAttribute(SESSION_STATE, state);

        log.info("Redirecting to OIDC provider with state: {}", appConfig.callbackUrl());

        String redirectUrl = UriBuilder.fromUri(appConfig.authorizationServerUrl())
            .queryParam("response_type", "code")
            .queryParam("client_id", appConfig.clientId())
            .queryParam("redirect_uri", appConfig.callbackUrl())
            .queryParam("scope", appConfig.scope())
            .queryParam("state", state)
            .build()
            .toString();

        try {
            log.info("Redirecting to {}", redirectUrl);
            response.sendRedirect(redirectUrl);
            return context.doNothing();
        }
        catch (IOException ex) {
            log.error("Redirect error", ex);
            throw new AuthenticationException("Error al redirigir a Keycloak", ex);
        }
    }

}