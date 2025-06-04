package com.mcm.samples.ui.infrastructure.client;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/rest/protected")
public class ProtectedResource {

    @Inject
    private SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response saludoProtegido() {
        String nombre = securityContext.getCallerPrincipal().getName();
        String mensaje = "Hola, " + nombre + "! Accediste a un recurso protegido.";
        return Response.ok("{\"mensaje\":\"" + mensaje + "\"}").build();
    }
}
