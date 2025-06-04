package com.mcm.samples.rest.client.infrastructure.security;

import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.mcm.samples.rest.client.infrastructure.config.AppConfig;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class CustomIdentityStoreHandler implements IdentityStoreHandler {

    @Inject
    private AppConfig appConfig;

    private JWKSet jwkSet;

    @PostConstruct
    public void init() throws IOException, ParseException {
        String jwkUri = appConfig.jwtUri();
        log.info("Using jwk uri: {}", jwkUri);
        jwkSet = JWKSet.load(new URL(jwkUri));
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        CustomCredential customCredential = (CustomCredential) credential;
        String accessToken = customCredential.getAccessToken();
        log.info("Access token: {}", accessToken);
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(accessToken);
        }
        catch (ParseException ex) {
            log.error("Invalid signature for access token: {}", accessToken);
            return CredentialValidationResult.INVALID_RESULT;
        }

        boolean validSignature = validateIdToken(signedJWT);
        if (!validSignature) {
            log.error("Invalid signature for access token: {}", accessToken);
            return CredentialValidationResult.INVALID_RESULT;
        }

        try {
            String username = signedJWT.getJWTClaimsSet().getStringClaim("name");
            Map realmAccess = (Map) signedJWT.getJWTClaimsSet().getClaim("realm_access");
            List<String> roles = (List<String>) realmAccess.get("roles");
            return new CredentialValidationResult(username, new HashSet<>(roles));
        }
        catch (Exception e) {
            log.error("Error parsing JWT claims", e);
            return CredentialValidationResult.INVALID_RESULT;
        }
    }

    public boolean validateIdToken(SignedJWT signedJWT) {
        try {
            JWK jwk = jwkSet.getKeyByKeyId(signedJWT.getHeader().getKeyID());
            if (jwk == null) {
                throw new IllegalStateException("No se encontró la clave pública para el kid: " + signedJWT.getHeader().getKeyID());
            }
            RSAKey rsaKey = (RSAKey) jwk;
            RSAPublicKey publicKey = rsaKey.toRSAPublicKey();
            JWSVerifier verifier = new RSASSAVerifier(publicKey);
            boolean signatureValid = signedJWT.verify(verifier);
            log.info("Signature validated: {}", signatureValid);
            return signatureValid;
        }
        catch (Exception ex) {
            log.error("Error validating token", ex);
            return false;
        }
    }

}
