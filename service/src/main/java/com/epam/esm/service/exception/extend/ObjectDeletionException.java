package com.epam.esm.service.exception.extend;

import com.epam.esm.service.exception.AbstractServiceException;

public class ObjectDeletionException extends AbstractServiceException {
    public ObjectDeletionException() {
    }

    public ObjectDeletionException(String message) {
        super(message);
    }

    public ObjectDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectDeletionException(Throwable cause) {
        super(cause);
    }

    public ObjectDeletionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
