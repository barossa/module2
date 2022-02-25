package com.epam.esm.exception.extend;

import com.epam.esm.exception.AbstractServiceException;

public class ObjectNotPresentedForDelete extends AbstractServiceException {
    public ObjectNotPresentedForDelete() {
    }

    public ObjectNotPresentedForDelete(String message) {
        super(message);
    }

    public ObjectNotPresentedForDelete(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotPresentedForDelete(Throwable cause) {
        super(cause);
    }

    public ObjectNotPresentedForDelete(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
