package com.mcm.samples.ui.infrastructure.security;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
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
public class CustomAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    private CustomIdentityStoreHandler identityStoreHandler;

    private static final String AUTH_SERVER_URL = "http://localhost:8090/realms/mcm-demo/protocol/openid-connect/auth";
    private static final String TOKEN_ENDPOINT = "http://localhost:8090/realms/mcm-demo/protocol/openid-connect/token";
    private static final String CLIENT_ID = "mcm-demo-client-jsf";
    private static final String CLIENT_SECRET = "HtsCdaR7o5KoNQpI0BOOWwtds1sxorCT";
    private static final String REDIRECT_URI = "http://localhost:8080/demo-ui/callback";
    private static final String SCOPE = "openid profile roles";

    private static final String PARAM_CODE = "code";
    private static final String SESSION_STATE = "OIDC_STATE";
    private static final String SESSION_NONCE = "OIDC_NONCE";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Jsonb jsonb = JsonbBuilder.create();

    // @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context)
        throws AuthenticationException {

        // if (!context.isProtected()) {
        //     log.info("Not required authentication for resource: {}", request.getRequestURI());
        //     // El recurso no requiere autenticación, dejar pasar la petición
        //     return context.doNothing();
        // }

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

        // if (request.getSession().getAttribute("principal") != null) {
        //     return context.doNothing();
        // }

        String path = request.getRequestURI().substring(request.getContextPath().length());
        String code = request.getParameter(PARAM_CODE);

        // 1. Si es el callback con ?code=..., procesamos el intercambio de tokens
        if (path.equals("/callback") && code != null) {
            log.info("Callback processing code {}", code);
            try {
                // Obtener el 'state' de la sesión si lo usas (aquí omitido)
                // Intercambiar code por tokens
                TokenResponse tokenResponse = readTokenFromIdP(code);

                if (tokenResponse != null && tokenResponse.id_token != null) {
                    // Validar ID Token (firma, issuer, audiencia, etc.)
                    // Para simplificar, aquí no implementamos validación completa de firma.
                    // En producción deberías:
                    //   - Descargar JWKS de Keycloak (https://.../protocol/openid-connect/certs)
                    //   - Validar firma del ID Token, issuer, client_id (aud), exp, nonce, etc.
                    // Podemos usar un IdentityStore que haga introspección del Access Token o validación JWT.

                    // Creamos un Credential “falso” para que nuestro IdentityStore valide el token.
                    CustomCredential oAuth2Cred = new CustomCredential(tokenResponse.access_token);

                    // Delegamos la validación a los IdentityStores registrados (p. ej. TokenIntrospectionIdentityStore)
                    // que extraerán usuario y roles. identityStoreHandler.validate(…) devolverá un 
                    // LoginResult con caller principal y grupos.
                    var result = identityStoreHandler.validate(oAuth2Cred);

                    log.info("Credential validation result: {}", result.getStatus());

                    if (result.getStatus() == CredentialValidationResult.Status.VALID) {
                        // Autenticación exitosa: fijamos el contexto y redirigimos a la URL original o a /
                        CallerPrincipal principal = result.getCallerPrincipal();
                        Set<String> groups = result.getCallerGroups();
                        log.info("Authentication successful for user: {} with groups {}", principal.getName(), groups);

                        //hack
                        request.getSession().setAttribute("access_token", tokenResponse.access_token);
                        request.getSession().setAttribute("id_token", tokenResponse.id_token);
                        request.getSession().setAttribute("username", principal.getName());
                        request.getSession().setAttribute("principal", principal);
                        request.getSession().setAttribute("groups", groups);

                        response.sendRedirect("http://localhost:8080/demo-ui/");
                        // Notificamos al contenedor de la autenticación exitosa

                        return context.notifyContainerAboutLogin(principal, groups);
                    }
                    else {
                        log.info("Unauthorized with validation result code: {}", result.getStatus());
                        // Token inválido o expirado
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

        log.info("Caller principal: {}", context.getCallerPrincipal());
        log.info("Caller auth parameters: {}", context.getAuthParameters());

        //TODO
        // // 2. Si el usuario ya está autenticado (ya hay un SecurityContext con Subject), dejamos pasar
        if (context.getCallerPrincipal() != null) {
            log.info("User already authenticated: {}", context.getCallerPrincipal().getName());
            return context.doNothing();
        }

        if (context.isAuthenticationRequest()) {
            log.info("Is an authentication request, but no code provided: {}", path);
            //     // Ya se está manejando otra cosa
            return context.doNothing();
        }

        // // 3. Si la ruta está protegida y no hay código, redirigimos al Authorization Endpoint
        // if (context.isAuthenticationRequest().equals(AuthenticationStatus.SEND_CONTINUE) ||
        //     context.isAuthenticationRequest().equals(AuthenticationStatus.SEND_FAILURE)) {
        //     // Ya se está manejando otra cosa
        //     return context.doNothing();
        // }

        // 3.1 Construir URL de redirección a Keycloak
        String state = UUID.randomUUID().toString();
        // Podrías generar un nonce para validarlo tras el callback
        // Guardamos state/nonce en sesión para luego validarlos
        request.getSession().setAttribute(SESSION_STATE, state);

        String redirectUrl = UriBuilder.fromUri(AUTH_SERVER_URL)
            .queryParam("response_type", "code")
            .queryParam("client_id", CLIENT_ID)
            .queryParam("redirect_uri", REDIRECT_URI)
            .queryParam("scope", SCOPE)
            .queryParam("state", state)
            // .queryParam("nonce", nonce) // si implementas validación de ID Token con nonce
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

    /**
     * Hace la petición HTTP al endpoint /token de Keycloak para intercambiar el code por
     * tokens. Devuelve un objeto con access_token, id_token, refresh_token, etc.
     */
    private TokenResponse readTokenFromIdP(String code) throws IOException, InterruptedException {
        log.info("Reading token from Keycloak for code {}", code);

        // Preparamos los parámetros del formulario x-www-form-urlencoded
        var form = new StringBuilder();
        form.append("grant_type=authorization_code");
        form.append("&code=").append(code);
        form.append("&redirect_uri=").append(REDIRECT_URI);
        form.append("&client_id=").append(CLIENT_ID);
        form.append("&client_secret=").append(CLIENT_SECRET);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(TOKEN_ENDPOINT))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(form.toString()))
            .build();

        HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            String body = resp.body();
            log.info("-- Token endpoint response start --/n{}/n-- Token endpoint response end --", body);
            return jsonb.fromJson(body, TokenResponse.class);
        }
        else {
            System.err.println("Error al obtener tokens: HTTP " + resp.statusCode() + " -> " + resp.body());
            return null;
        }
    }

    /**
     * Clase interna para mapear la respuesta JSON del token endpoint de Keycloak.
     */
    public static class TokenResponse {
        public String access_token;
        public String expires_in;
        public String refresh_expires_in;
        public String refresh_token;
        public String token_type;
        public String not_before_policy;
        public String session_state;
        public String scope;
        public String id_token;
    }
}