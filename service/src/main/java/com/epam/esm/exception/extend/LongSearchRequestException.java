package com.epam.esm.exception.extend;

import com.epam.esm.exception.AbstractServiceException;

public class LongSearchRequestException extends AbstractServiceException {
    public LongSearchRequestException() {
    }

    public LongSearchRequestException(String message) {
        super(message);
    }

    public LongSearchRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public LongSearchRequestException(Throwable cause) {
        super(cause);
    }

    public LongSearchRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
