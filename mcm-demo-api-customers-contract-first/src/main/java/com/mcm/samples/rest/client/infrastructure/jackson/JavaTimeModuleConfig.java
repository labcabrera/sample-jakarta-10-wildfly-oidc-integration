package com.mcm.samples.rest.client.infrastructure.jackson;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class JavaTimeModuleConfig extends SimpleModule {

    public JavaTimeModuleConfig() {
        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
    }
}