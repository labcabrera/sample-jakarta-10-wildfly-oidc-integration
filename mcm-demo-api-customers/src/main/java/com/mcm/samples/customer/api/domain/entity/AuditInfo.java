package com.mcm.samples.customer.api.domain.entity;

import java.time.LocalDateTime;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@JsonbPropertyOrder({ "createdAt", "updatedAt", "createdBy", "updatedBy" })
public class AuditInfo {

    @NotNull
    @Schema(description = "Creation timestamp of the customer record", example = "2023-10-01T12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp of the customer record", example = "2023-10-01T12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;

    @Schema(description = "User who created the customer record", example = "johndoe")
    private String createdBy;

    @Schema(description = "User who last updated the customer record", example = "johndoe")
    private String updatedBy;

}
