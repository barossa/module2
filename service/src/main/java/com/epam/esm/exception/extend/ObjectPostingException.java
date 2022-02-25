package com.epam.esm.exception.extend;

import com.epam.esm.exception.AbstractServiceException;

public class ObjectPostingException extends AbstractServiceException {
    public ObjectPostingException() {
    }

    public ObjectPostingException(String message) {
        super(message);
    }

    public ObjectPostingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectPostingException(Throwable cause) {
        super(cause);
    }

    public ObjectPostingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
