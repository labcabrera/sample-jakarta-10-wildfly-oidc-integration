package com.mcm.samples.customer.api.domain.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiErrorDetail {

    @Schema(description = "Error message", example = "Invalid input data")
    private String message;

    @Schema(description = "Detailed information about the error", example = "Email already exists")
    private String detail;
}
