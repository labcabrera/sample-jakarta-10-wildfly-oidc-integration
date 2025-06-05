package com.mcm.samples.rest.client.infrastructure.jpa.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String firstName;

    private String lastName;

    @Embedded
    private CustomerContactInfoEntity contactInfo;

    private String status;

    private Boolean emailVerified;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}