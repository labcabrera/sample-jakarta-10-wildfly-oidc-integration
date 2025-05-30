package com.mcm.samples.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecuredController {

    @GetMapping("/secured")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        token.getPrincipal().getAttribute("preferred_username");

        List<String> authorities = authentication.getAuthorities().stream().map(e -> e.getAuthority().toString())
            .collect(Collectors.toList());

        model.addAttribute("message", "This is a sample generated message from the secured controller.");
        model.addAttribute("principal", authentication.getPrincipal());
        model.addAttribute("authorities", authorities);
        model.addAttribute("token", token);
        model.addAttribute("username", token.getPrincipal().getAttribute("preferred_username"));
        model.addAttribute("email", token.getPrincipal().getAttribute("email"));
        return "secured";
    }
}
