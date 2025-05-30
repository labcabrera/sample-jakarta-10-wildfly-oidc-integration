package com.mcm.samples.rest.client.domain.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Claim {

    private String id;

    private LocalDate claimDate;

    private String description;
}
