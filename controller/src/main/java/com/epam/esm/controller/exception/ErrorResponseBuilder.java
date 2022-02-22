package com.epam.esm.controller.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class ErrorResponseBuilder {
    private final MessageSource messageSource;

    public ErrorResponseBuilder(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ErrorResponse build(ErrorCode error) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(error.toString(), new Object[]{}, locale);
        return new ErrorResponse(LocalDateTime.now(), error.getErrorCode(), message, null);
    }
}
