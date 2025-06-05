package com.mcm.samples.rest.client.domain.entity;

import java.time.LocalDateTime;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonbPropertyOrder({ "id", "firstName", "lastName", "contactInfo", "status", "createdAt", "updatedAt", "createdBy",
    "updatedBy" })
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

    @JsonbTypeAdapter(CustomerStatusAdapter.class)
    @NotNull
    @Schema(description = "Status of the customer", example = "ACTIVE")
    private CustomerStatus status;

    @NotNull
    @Schema(description = "Creation timestamp of the customer record", example = "2023-10-01T12:00:00")
    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp of the customer record", example = "2023-10-01T12:00:00")
    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;

    @Schema(description = "User who created the customer record", example = "johndoe")
    private String createdBy;

    @Schema(description = "User who last updated the customer record", example = "johndoe")
    private String updatedBy;

}
