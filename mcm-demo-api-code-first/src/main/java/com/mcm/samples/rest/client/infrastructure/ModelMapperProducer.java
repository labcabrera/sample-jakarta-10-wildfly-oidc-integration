package com.mcm.samples.rest.client.infrastructure;

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
