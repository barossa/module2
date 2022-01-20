package com.epam.esm.controller.error;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class ErrorResponseBuilder {
    public static final int OBJECT_NOT_FOUND = 40401;
    public static final int INTERNAL_ERROR = 50001;
    public static final int OBJECT_POSTING_ERROR = 50002;
    public static final int OBJECT_ALREADY_EXISTS = 40901;

    private final MessageSource messageSource;

    public ErrorResponseBuilder(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ErrorResponse build(int errorCode) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(String.valueOf(errorCode), new Object[]{}, locale);
        return new ErrorResponse(LocalDateTime.now(), errorCode, message);
    }
}
