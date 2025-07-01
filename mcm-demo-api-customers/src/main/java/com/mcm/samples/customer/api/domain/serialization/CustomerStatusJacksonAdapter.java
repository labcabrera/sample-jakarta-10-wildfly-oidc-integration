package com.mcm.samples.customer.api.domain.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mcm.samples.customer.api.domain.model.CustomerStatus;

public class CustomerStatusJacksonAdapter {

    public static class Serializer extends JsonSerializer<CustomerStatus> {
        @Override
        public void serialize(CustomerStatus value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.getCode());
        }
    }

    public static class Deserializer extends JsonDeserializer<CustomerStatus> {
        @Override
        public CustomerStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return CustomerStatus.valueOf(p.getText().toUpperCase());
        }
    }
}