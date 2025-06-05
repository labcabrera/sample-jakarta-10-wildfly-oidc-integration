package com.mcm.samples.ui.infrastructure.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login**", "/error**", "/css/**", "/").permitAll()
                .anyRequest().authenticated())
            .oauth2Login((oauth2Login) -> oauth2Login
                .userInfoEndpoint((userInfo) -> userInfo
                    .userAuthoritiesMapper(grantedAuthoritiesMapper())));
        return http.build();
    }

    private GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach((authority) -> {
                GrantedAuthority mappedAuthority;

                if (authority instanceof OidcUserAuthority) {
                    OidcUserAuthority userAuthority = (OidcUserAuthority) authority;
                    mappedAuthority = new OidcUserAuthority(
                        "OIDC_USER", userAuthority.getIdToken(), userAuthority.getUserInfo());
                }
                else if (authority instanceof OAuth2UserAuthority) {
                    OAuth2UserAuthority userAuthority = (OAuth2UserAuthority) authority;
                    mappedAuthority = new OAuth2UserAuthority(
                        "OAUTH2_USER", userAuthority.getAttributes());
                }
                else {
                    mappedAuthority = authority;
                }

                try {
                    if (authority instanceof OidcUserAuthority) {
                        OidcUserAuthority userAuthority = (OidcUserAuthority) authority;
                        if (userAuthority.getAttributes().containsKey("groups")) {
                            log.info("Mapping groups: {} ", userAuthority.getAttributes().get("groups"));
                            List<String> groups = (List<String>) userAuthority.getAttributes().get("groups");
                            for (String group : groups) {
                                mappedAuthorities.add(() -> "ROLE_" + group.toUpperCase());
                            }
                        }
                    }
                }
                catch (Exception ex) {
                    log.error("Error mapping groups from authority: {}", authority, ex);
                }

                mappedAuthorities.add(mappedAuthority);
            });

            return mappedAuthorities;
        };
    }

}