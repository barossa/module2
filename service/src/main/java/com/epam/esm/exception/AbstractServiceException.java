package com.epam.esm.exception;

public abstract class AbstractServiceException extends RuntimeException {
    protected AbstractServiceException() {
    }

    protected AbstractServiceException(String message) {
        super(message);
    }

    protected AbstractServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    protected AbstractServiceException(Throwable cause) {
        super(cause);
    }

    protected AbstractServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
