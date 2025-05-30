package com.mcm.samples.ui.infrastructure.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SecuredController {

    @GetMapping("/secured")
    public String home(Model model, @AuthenticationPrincipal OidcUser oidcUser) {
        String jwt = oidcUser.getIdToken().getTokenValue();

        log.info("auth: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        SecurityContextHolder.getContext().getAuthentication().getAuthorities().forEach(auth -> {
            log.info("Authority: {}", auth.getAuthority());
        });

        List<String> authorities = oidcUser.getAuthorities().stream().map(e -> e.getAuthority().toString())
            .collect(Collectors.toList());

        model.addAttribute("message", "This is a sample generated message from the secured controller.");
        model.addAttribute("authorities", authorities);
        model.addAttribute("jwt", jwt);
        model.addAttribute("username", oidcUser.getFullName());
        model.addAttribute("email", oidcUser.getEmail());
        return "secured";
    }
}
