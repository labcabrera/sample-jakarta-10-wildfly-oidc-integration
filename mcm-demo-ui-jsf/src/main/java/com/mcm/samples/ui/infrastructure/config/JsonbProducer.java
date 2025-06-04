package com.mcm.samples.ui.infrastructure.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@ApplicationScoped
public class JsonbProducer {

    @Produces
    public Jsonb getHttpClient() {
        return JsonbBuilder.create();
    }
}
