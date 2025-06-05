package com.mcm.samples.customer.api.domain.entity;

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

    private String email;

    private String phoneNumber;

}
