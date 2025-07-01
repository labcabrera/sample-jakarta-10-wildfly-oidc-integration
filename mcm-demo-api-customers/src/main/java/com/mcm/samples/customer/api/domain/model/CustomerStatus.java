package com.mcm.samples.customer.api.domain.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Estado del cliente", enumeration = {
    "pending_activation", "active", "inactive", "deleted", "suspended" })
public enum CustomerStatus {

    PENDING_ACTIVATION("pending_activation"),

    ACTIVE("active"),

    INACTIVE("inactive"),

    DELETED("deleted"),

    SUSPENDED("suspended");

    private final String code;

    CustomerStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
