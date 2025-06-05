package com.mcm.samples.customer.api.infrastructure.config;

import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ModelMapperProducer {

    @Produces
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
