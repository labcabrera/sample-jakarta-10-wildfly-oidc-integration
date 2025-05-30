package com.mcm.samples.ui;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LogoutController {

    @GetMapping("/user/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            new SecurityContextLogoutHandler().logout(request, response, null);
            String issuer = principal.getIssuer().toString();
            String idToken = principal.getIdToken().getTokenValue();
            String redirectUri = "http://localhost:8085/";

            String logoutUrl = issuer + "/protocol/openid-connect/logout"
                + "?post_logout_redirect_uri=" + redirectUri
                + "&id_token_hint=" + idToken;

            return "redirect:" + logoutUrl;
        }
        return "redirect:/";
    }
}