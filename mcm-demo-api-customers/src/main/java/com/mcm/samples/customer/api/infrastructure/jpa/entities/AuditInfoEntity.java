package com.mcm.samples.customer.api.infrastructure.jpa.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditInfoEntity {

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NotNull
    private String createdBy;

    private String updatedBy;
}
