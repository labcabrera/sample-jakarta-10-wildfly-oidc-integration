package com.mcm.samples.rest.client.infrastructure.jpa.mapper;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;

import com.mcm.samples.rest.client.domain.entity.Customer;
import com.mcm.samples.rest.client.infrastructure.jpa.entities.CustomerEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CustomerMapper {

    @Inject
    private ModelMapper modelMapper;

    public Customer toDomain(CustomerEntity entity) {
        if (entity == null)
            return null;
        return modelMapper.map(entity, Customer.class);
    }

    public CustomerEntity toEntity(Customer domain) {
        if (domain == null)
            return null;
        CustomerEntity entity = modelMapper.map(domain, CustomerEntity.class);
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }
}