package com.mcm.samples.customer.api.infrastructure.jpa.mapper;

import org.modelmapper.ModelMapper;

import com.mcm.samples.customer.api.domain.entity.Customer;
import com.mcm.samples.customer.api.infrastructure.jpa.entities.CustomerEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CustomerMapper {

    @Inject
    private ModelMapper modelMapper;

    public Customer toDomain(CustomerEntity entity) {
        if (entity == null) {
            return null;
        }
        return modelMapper.map(entity, Customer.class);
    }

    public CustomerEntity toEntity(Customer domain) {
        if (domain == null) {
            return null;
        }
        return modelMapper.map(domain, CustomerEntity.class);
    }

}
