package com.dtstep.lighthouse.insights.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerdeConfig.LocalDateTimeToEpochSerializer());
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateTimeSerdeConfig.LocalDateToEpochSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeSerdeConfig.LocalDateTimeFromEpochDeserializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateTimeSerdeConfig.LocalDateFromEpochDeserializer());
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}