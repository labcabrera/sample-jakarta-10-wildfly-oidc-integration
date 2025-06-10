package com.mcm.samples.customer.api.domain.entity;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mcm.samples.customer.api.domain.serialization.CustomerStatusJacksonAdapter;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonbPropertyOrder({ "id", "firstName", "lastName", "contactInfo", "status", "auditInfo" })
public class Customer {

    @NotNull
    @Schema(description = "Unique identifier of the customer", example = "b41909ed-57d4-4342-92a7-3445c0fb19f8")
    private String id;

    @NotNull
    @Schema(description = "First name of the customer", example = "John")
    private String firstName;

    @NotNull
    @Schema
    private String lastName;

    @NotNull
    @Schema(description = "Contact information of the customer")
    private CustomerContactInfo contactInfo;

    @JsonSerialize(using = CustomerStatusJacksonAdapter.Serializer.class)
    @JsonDeserialize(using = CustomerStatusJacksonAdapter.Deserializer.class)

    @NotNull
    @Schema(description = "Status of the customer", example = "ACTIVE")
    private CustomerStatus status;

    @NotNull
    @Schema(description = "Entity audit information.")
    private AuditInfo auditInfo;

}
