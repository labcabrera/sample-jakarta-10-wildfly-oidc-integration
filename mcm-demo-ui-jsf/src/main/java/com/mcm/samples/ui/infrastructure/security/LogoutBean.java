package com.mcm.samples.ui.infrastructure.security;

import java.io.IOException;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Named
@RequestScoped
@Slf4j
public class LogoutBean {

    public void logout() throws IOException {
        log.info("Loging out user");

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        String idTokenHint = (String) request.getSession().getAttribute("access_token");

        log.info("Access token: {}", idTokenHint);

        // Invalida la sesión local
        request.getSession().invalidate();

        // Redirige al endpoint de logout de Keycloak
        String keycloakLogout = "http://localhost:8090/realms/mcm-demo/protocol/openid-connect/logout"
            + "?post_logout_redirect_uri=http://localhost:8080/demo-ui/"
            + "&id_token_hint=" + idTokenHint;

        //             GET
        // http://localhost:8090/realms/mcm-demo/protocol/openid-connect/logout?post_logout_redirect_uri=http://localhost:8085/&id_token_hint=eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJzclhDeGxFMVEwczhESlhfYVFtU0doM2JNODk2V1FwZ3dQV2YxalhTbE5nIn0.eyJleHAiOjE3NDkwNDU1NDksImlhdCI6MTc0OTA0NTI0OSwiYXV0aF90aW1lIjoxNzQ5MDQ1MjQ5LCJqdGkiOiJjMmRlYzAxNy0xN2E5LTRmMjQtYjE1MS03YmIwNGI5MjFiZjEiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwOTAvcmVhbG1zL21jbS1kZW1vIiwiYXVkIjoibWNtLWRlbW8tY2xpZW50Iiwic3ViIjoiMTkwNTg3NTktM2Q0Ny00YjRjLTllOGItZDFhNzAyYzNmYmE1IiwidHlwIjoiSUQiLCJhenAiOiJtY20tZGVtby1jbGllbnQiLCJub25jZSI6ImVsMjY5bWhudDRtOWRFLWhTNFEyejZKLUMwcldoclRLdTVHTHN1OXpOX1UiLCJzZXNzaW9uX3N0YXRlIjoiMjk3OWVhMGYtYzdlYy00ZjJhLWFhMzQtZTQ3MjVlNmNiNjU3IiwiYXRfaGFzaCI6Ikk4ekRUbTk0U3VOZmVtUk1ibm9IMHciLCJhY3IiOiIxIiwic2lkIjoiMjk3OWVhMGYtYzdlYy00ZjJhLWFhMzQtZTQ3MjVlNmNiNjU3IiwidXBuIjoiam9obmRvZSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiSm9obiBEb2UiLCJncm91cHMiOlsiYWRtaW4iLCJkZWZhdWx0LXJvbGVzLW1jbS1kZW1vIiwidXNlciJdLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqb2huZG9lIiwiZ2l2ZW5fbmFtZSI6IkpvaG4iLCJmYW1pbHlfbmFtZSI6IkRvZSIsImVtYWlsIjoiam9obmRvZUBkZW1vLmNvbSJ9.2_DjwFqsuKcwO91BdugzUZ2ILdE0hgIjPCK1cEqzQqTGphwDg9wPRf_NpFB8dvosp417um0YEe0hJdjRcMSYe7HeGAWAqjymyLJ1SkQiHNfMSJXtEsXRjwvAwSxKoyVpaHHYGs7xP-OxFt0GMjGFazSXRjqgnKegaxudGds7Xlo2nvJknVZbhhk7UQe612bl8eNVNZ1pb69uFPFiJSccZ0O0SW9gzpAkCh3_epGveR9dyLvQj8X_HqjYyOZ2M0JIdaKGslaA13I_NLDg_32Xull_OgEDBdbELBF_tPIWE1Js00YhiUH50uKXUCvgvXuHUvckeUj-lNoew53Ed7OJIQ

        context.getExternalContext().redirect(keycloakLogout);
    }
}
