package com.mcm.samples.customer.api.infrastructure.api;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.mcm.samples.customer.api.domain.cmd.CreateCustomerCmd;
import com.mcm.samples.customer.api.domain.cmd.UpdateCustomerCmd;
import com.mcm.samples.customer.api.domain.entity.ApiError;
import com.mcm.samples.customer.api.domain.entity.Customer;
import com.mcm.samples.customer.api.domain.entity.CustomerPage;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/customers")
@Tag(name = "Customers", description = "Customers Demo REST API")
public interface CustomerApiDefinition {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find customer by id", description = "Find customer by id", operationId = "findCustomerById")
    @APIResponse(responseCode = "200", description = "Customer found", content = @Content(schema = @Schema(implementation = Customer.class)))
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Response findById(@PathParam("id") String id);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find customers by search expression", description = "Find customers by search expression", operationId = "findCustomers")
    @APIResponse(responseCode = "200", description = "Customer list", content = @Content(schema = @Schema(implementation = CustomerPage.class)))
    public Response find(
        @QueryParam("query") @DefaultValue("") @Parameter(description = "Search expression (RSQL)", example = "firstName==John") String searchExpression,
        @QueryParam("page") @DefaultValue("0") @Parameter(description = "Page count", example = "0") Integer page,
        @QueryParam("size") @DefaultValue("10") @Parameter(description = "Page size", example = "10") Integer size);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create consumer", description = "Create consumer", operationId = "createConsumer")
    @APIResponse(responseCode = "201", description = "Customer created", content = @Content(schema = @Schema(implementation = Customer.class)))
    @APIResponse(responseCode = "400", description = "Validation errors", content = @Content(schema = @Schema(implementation = ApiError.class)))
    public Response createConsumer(CreateCustomerCmd cmd);

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update consumer", description = "Update consumer", operationId = "updateConsumer")
    @APIResponse(responseCode = "202", description = "Customer updated", content = @Content(schema = @Schema(implementation = Customer.class)))
    @APIResponse(responseCode = "400", description = "Validation errors", content = @Content(schema = @Schema(implementation = ApiError.class)))
    public Response updateConsumer(@PathParam("id") String id, UpdateCustomerCmd cmd);

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete customer by id", description = "Delete customer by id", operationId = "deleteCustomer")
    public Response delete(@PathParam("id") String id);
}