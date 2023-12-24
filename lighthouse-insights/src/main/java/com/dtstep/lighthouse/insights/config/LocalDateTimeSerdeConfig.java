package com.dtstep.lighthouse.insights.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Configuration
public class LocalDateTimeSerdeConfig {

    public static class LocalDateTimeToEpochSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            if (value != null) {
                long timestamp = value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                gen.writeNumber(timestamp);
            }
        }
    }

    public static class LocalDateToEpochSerializer extends JsonSerializer<LocalDate> {

        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            if (value != null) {
                long timestamp = value.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                gen.writeNumber(timestamp);
            }
        }
    }

    public static class LocalDateTimeFromEpochDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            NumberDeserializers.LongDeserializer longDeserializer = new NumberDeserializers.LongDeserializer(Long.TYPE, 0L);
            Long epoch = longDeserializer.deserialize(p, ctxt);
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
        }
    }

    public static class LocalDateFromEpochDeserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            NumberDeserializers.LongDeserializer longDeserializer = new NumberDeserializers.LongDeserializer(Long.TYPE, 0L);
            Long epoch = longDeserializer.deserialize(p, ctxt);
            return LocalDate.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
        }
    }
}
