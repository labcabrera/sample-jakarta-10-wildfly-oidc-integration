package com.mcm.samples.customer.api.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<ApiErrorDetail> details;
}
