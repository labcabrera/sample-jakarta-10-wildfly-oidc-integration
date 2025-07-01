package com.mcm.samples.customer.api.domain.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonbPropertyOrder({ "email", "phoneNumber" })
public class CustomerContactInfo {

    @Schema(description = "Email address of the customer", example = "johndoe.@example.com")
    private String email;

    @Schema(description = "Phone number of the customer", example = "666778899")
    private String phoneNumber;

}
