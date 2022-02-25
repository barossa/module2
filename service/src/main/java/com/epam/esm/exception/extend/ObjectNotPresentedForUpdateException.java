package com.epam.esm.exception.extend;

import com.epam.esm.exception.AbstractServiceException;

public class ObjectNotPresentedForUpdateException extends AbstractServiceException {
    public ObjectNotPresentedForUpdateException() {
    }

    public ObjectNotPresentedForUpdateException(String message) {
        super(message);
    }

    public ObjectNotPresentedForUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotPresentedForUpdateException(Throwable cause) {
        super(cause);
    }

    public ObjectNotPresentedForUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
