package com.mcm.samples.security.oidc;

import lombok.Data;

@Data
public class OidcTokenResponse {

    private String access_token;
    private String expires_in;
    private String refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private String not_before_policy;
    private String session_state;
    private String scope;
    private String id_token;
}
