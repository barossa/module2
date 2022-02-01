package com.epam.esm.service.config;

import com.epam.esm.service.validator.ErrorMessageProvider;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class ServiceConfig {
    private static final String VALIDATOR_BUNDLE_NAME = "lang/validator";
    private static final String VALIDATOR_ENCODING = "UTF-8";

    @Bean(name = "validatorMessageSource")
    public MessageSource validatorMessageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(VALIDATOR_BUNDLE_NAME);
        messageSource.setDefaultEncoding(VALIDATOR_ENCODING);
        return messageSource;
    }
}
