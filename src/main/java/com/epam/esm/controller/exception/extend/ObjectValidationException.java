package com.epam.esm.controller.exception.extend;

import com.epam.esm.controller.exception.AbstractServiceException;
import com.epam.esm.controller.exception.ErrorCode;

import java.util.List;

public class ObjectValidationException extends AbstractServiceException {
    {
        errorCode = ErrorCode.OBJECT_VALIDATION_ERROR;
    }

    private final List<String> errors;

    public ObjectValidationException(List<String> errors) {
        this.errors = errors;
    }

    public ObjectValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public ObjectValidationException(String message, Throwable cause, List<String> errors) {
        super(message, cause);
        this.errors = errors;
    }

    public ObjectValidationException(Throwable cause, List<String> errors) {
        super(cause);
        this.errors = errors;
    }

    public ObjectValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, List<String> errors) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
