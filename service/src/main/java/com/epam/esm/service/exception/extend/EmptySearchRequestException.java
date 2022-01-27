package com.epam.esm.service.exception.extend;

import com.epam.esm.service.exception.AbstractServiceException;

public class EmptySearchRequestException extends AbstractServiceException {
    public EmptySearchRequestException() {
    }

    public EmptySearchRequestException(String message) {
        super(message);
    }

    public EmptySearchRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptySearchRequestException(Throwable cause) {
        super(cause);
    }

    public EmptySearchRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
