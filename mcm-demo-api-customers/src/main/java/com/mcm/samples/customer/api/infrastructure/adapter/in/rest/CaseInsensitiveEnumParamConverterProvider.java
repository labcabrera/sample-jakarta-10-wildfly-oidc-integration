package com.mcm.samples.customer.api.infrastructure.adapter.in.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CaseInsensitiveEnumParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(final Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.isEnum()) {
            return new ParamConverter<T>() {
                @Override
                public T fromString(String value) {
                    if (value == null)
                        return null;
                    for (T c : rawType.getEnumConstants()) {
                        if (c.toString().equalsIgnoreCase(value)) {
                            return c;
                        }
                    }
                    throw new IllegalArgumentException("Unknown enum value: " + value);
                }

                @Override
                public String toString(T value) {
                    return value.toString();
                }
            };
        }
        return null;
    }
}