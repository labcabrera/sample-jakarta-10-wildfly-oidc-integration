package com.mcm.samples.security.oidc;

import java.io.IOException;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Named
@RequestScoped
@Slf4j
public class OidcLogoutBean {

    @Inject
    private OidcConfig oidcConfig;

    public void logout() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String idTokenHint = (String) request.getSession().getAttribute("id_token");
        request.getSession().invalidate();
        String redirectUrl = oidcConfig.logoutRedirectUrl();
        String logoutUrl = oidcConfig.logoutUrl();
        StringBuilder sb = new StringBuilder(logoutUrl);
        sb.append("?post_logout_redirect_uri=").append(redirectUrl);
        sb.append("&id_token_hint=").append(idTokenHint);
        context.getExternalContext().redirect(sb.toString());
    }
}
