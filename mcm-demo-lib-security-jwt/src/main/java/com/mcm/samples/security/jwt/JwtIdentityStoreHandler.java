package com.mcm.samples.security.jwt;

import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
public class JwtIdentityStoreHandler implements IdentityStoreHandler {

    @Inject
    private JwtConfig appConfig;

    @Inject
    private JwtUserInfoMapper userInfoMapper;

    private JWKSet jwkSet;

    @PostConstruct
    public void init() {
        try {
            String jwkUri = appConfig.jwtUri();
            log.info("Using JWK uri: {}", jwkUri);
            jwkSet = JWKSet.load(new URL(jwkUri));
        }
        catch (IOException | ParseException ex) {
            throw new RuntimeException("Error reading JWK set", ex);
        }
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        JwtCredential customCredential = (JwtCredential) credential;
        String accessToken = customCredential.getAccessToken();
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
            Set<String> roles = getRolesFromClaims(signedJWT);
            return mapCredentialValidationResult(username, roles);
        }
        catch (Exception e) {
            log.error("Error parsing JWT claims", e);
            return CredentialValidationResult.INVALID_RESULT;
        }
    }

    private boolean validateIdToken(SignedJWT signedJWT) {
        try {
            String key = signedJWT.getHeader().getKeyID();
            JWK jwk = jwkSet.getKeyByKeyId(key);
            if (jwk == null) {
                throw new RuntimeException(String.format("Missing public key '%s'", key));
            }
            RSAKey rsaKey = (RSAKey) jwk;
            RSAPublicKey publicKey = rsaKey.toRSAPublicKey();
            JWSVerifier verifier = new RSASSAVerifier(publicKey);
            return signedJWT.verify(verifier);
        }
        catch (Exception ex) {
            log.error("Error validating token", ex);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private Set<String> getRolesFromClaims(SignedJWT signedJWT) {
        try {
            Map<String, Object> realmAccess = (Map<String, Object>) signedJWT.getJWTClaimsSet().getClaim("realm_access");
            if (realmAccess != null) {
                List<String> roles = (List<String>) realmAccess.get("roles");
                log.info("Readed roles from JWT claims: {}", roles);
                return new HashSet<>(roles);
            }
        }
        catch (ParseException e) {
            log.error("Error parsing JWT claims", e);
        }
        return new HashSet<>();
    }

    private CredentialValidationResult mapCredentialValidationResult(String username, Set<String> roles) {
        log.info("Using user info mapper to map username: {}", username);
        Entry<String, List<String>> userInfo = userInfoMapper.map(username);
        String mappedUsername = userInfo.getKey();
        roles.addAll(userInfo.getValue());
        return new CredentialValidationResult(mappedUsername, roles);
    }

}
