package com.epam.esm.controller.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.sql.DataSource;
import java.util.Locale;

@Configuration
@ComponentScan(value = {"com.epam.esm"})
@EnableWebMvc
public class AppConfiguration {
    private static final String MESSAGES_BUNDLE_NAME = "lang/messages";
    private static final String MESSAGES_ENCODING = "UTF-8";

    private static final Locale DEFAULT_LOCALE = Locale.US;

    public AppConfiguration(RequestMappingHandlerAdapter adapter) {
        adapter.getMessageConverters()
                .forEach(converter -> {
                    if (converter instanceof MappingJackson2HttpMessageConverter) {
                        MappingJackson2HttpMessageConverter jacksonConverter = (MappingJackson2HttpMessageConverter) converter;
                        ObjectMapper objectMapper = jacksonConverter.getObjectMapper();
                        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    }
                });
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
        acceptHeaderLocaleResolver.setDefaultLocale(DEFAULT_LOCALE);
        return acceptHeaderLocaleResolver;
    }
}

