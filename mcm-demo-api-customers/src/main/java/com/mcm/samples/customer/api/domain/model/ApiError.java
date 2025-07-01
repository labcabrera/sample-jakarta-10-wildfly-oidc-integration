package com.mcm.samples.customer.api.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {

    @Schema(description = "Error code representing the type of error", example = "400")
    private String code;

    @Schema(description = "Human-readable message describing the error", example = "Invalid customer creation request")
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Schema(description = "Timestamp when the error occurred", example = "2023-10-01T12:00:00.000")
    private LocalDateTime timestamp;

    @Schema(description = "List of detailed error messages or validation errors")
    private List<ApiErrorDetail> details;
}
