package com.mcm.samples.security.oidc;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
public class OidcIdentityStoreHandler implements IdentityStoreHandler {

    @Inject
    private OidcConfig appConfig;

    @Inject
    private OidcUserInfoMapper userInfoMapper;

    private JWKSet jwkSet;

    @PostConstruct
    public void init() {
        String jwtUrl = appConfig.jwkUri();
        log.info("Initializing OidcIdentityStoreHandler using keystore '{}'", jwtUrl);
        try {
            jwkSet = JWKSet.load(new URL(jwtUrl));
        }
        catch (Exception ex) {
            throw new OidcInvalidConfigurationException("Failed to load JWK set", ex);
        }
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public CredentialValidationResult validate(Credential credential) {
        OidcCredential customCredential = (OidcCredential) credential;
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
            String username = signedJWT.getJWTClaimsSet().getStringClaim("preferred_username");
            Map realmAccess = (Map) signedJWT.getJWTClaimsSet().getClaim("realm_access");
            List<String> roles = (List<String>) realmAccess.get("roles");
            return mapCredentialValidationResult(username, roles);
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

    private CredentialValidationResult mapCredentialValidationResult(String username, List<String> roles) {
        log.info("Using user info mapper to map username: {}", username);
        Entry<String, List<String>> userInfo = userInfoMapper.map(username);
        String mappedUsername = userInfo.getKey();
        roles.addAll(userInfo.getValue());
        return new CredentialValidationResult(mappedUsername, new HashSet<>(roles));
    }

}
