package com.epam.esm.config;

import com.epam.esm.controller.exception.ErrorResponseBuilder;
import com.epam.esm.controller.exception.NotFoundHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.sql.DataSource;
import java.util.Locale;

@Configuration
@ComponentScan(value = {"com.epam.esm"})
public class AppConfiguration {
    private static final String MESSAGES_BUNDLE_NAME = "lang/messages";
    private static final String MESSAGES_ENCODING = "UTF-8";

    public AppConfiguration(ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(MESSAGES_BUNDLE_NAME);
        messageSource.setDefaultEncoding(MESSAGES_ENCODING);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
        acceptHeaderLocaleResolver.setDefaultLocale(Locale.US);
        return acceptHeaderLocaleResolver;
    }

    @Bean("/*")
    public HttpRequestHandler notFoundHandler(ErrorResponseBuilder responseBuilder, ObjectMapper objectMapper) {
        return new NotFoundHandler(responseBuilder, objectMapper);
    }

}
