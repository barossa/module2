package com.epam.esm.service.validator.impl;

import com.epam.esm.service.validator.ErrorMessageProvider;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ErrorMessageProviderImpl implements ErrorMessageProvider {
    private static final String ERROR_MESSAGE_PREFIX = "validator.";

    private final MessageSource messageSource;

    public ErrorMessageProviderImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(ERROR_MESSAGE_PREFIX + code, new Object[]{}, locale);
    }
}
