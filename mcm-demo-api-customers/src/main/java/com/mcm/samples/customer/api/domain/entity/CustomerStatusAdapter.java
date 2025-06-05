package com.mcm.samples.customer.api.domain.entity;

import jakarta.json.bind.adapter.JsonbAdapter;

public class CustomerStatusAdapter implements JsonbAdapter<CustomerStatus, String> {

    @Override
    public String adaptToJson(CustomerStatus status) {
        return status.getDescription();
    }

    @Override
    public CustomerStatus adaptFromJson(String value) {
        for (CustomerStatus status : CustomerStatus.values()) {
            if (status.getDescription().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}