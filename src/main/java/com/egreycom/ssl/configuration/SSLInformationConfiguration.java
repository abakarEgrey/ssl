package com.egreycom.ssl.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Log4j2
public class SSLInformationConfiguration {
    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper result = new ObjectMapper();
        result.findAndRegisterModules();
        result.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        result.registerModule(new ParameterNamesModule());
        result.registerModule(new Jdk8Module());
        result.registerModule(new JavaTimeModule());
        result.enable(SerializationFeature.INDENT_OUTPUT);
        result.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        result.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return result;
    }
}
