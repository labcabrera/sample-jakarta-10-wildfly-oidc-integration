package com.mcm.samples.customer.api.domain.entity;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Estado del cliente", enumeration = {
    "pending_activation", "active", "inactive", "deleted", "suspended" })
public enum CustomerStatus {

    PENDING_ACTIVATION("pending_activation"),

    ACTIVE("active"),

    INACTIVE("inactive"),

    DELETED("deleted"),

    SUSPENDED("suspended");

    private final String description;

    CustomerStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
