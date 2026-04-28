package com.tribune.demo.km.config;

import org.bson.types.ObjectId;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;

/**
 * Custom Jackson serializer/deserializer for MongoDB ObjectId
 * Serializes ObjectId as hex string, deserializes hex string to ObjectId
 */
public class ObjectIdHandler {

    public static class Serializer extends ValueSerializer<ObjectId> {
        @Override
        public void serialize(ObjectId value, tools.jackson.core.JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
            if (value == null) {
                gen.writeNull();
            } else {
                gen.writeString(value.toHexString());
            }
        }
    }

    public static class Deserializer extends ValueDeserializer<ObjectId> {

        @Override
        public ObjectId deserialize(tools.jackson.core.JsonParser p, tools.jackson.databind.DeserializationContext ctxt) throws JacksonException {
            String value = p.getValueAsString();
            if (value == null || value.isEmpty()) {
                return null;
            }
            return new ObjectId(value);
        }
    }
}

