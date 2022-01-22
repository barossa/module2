package com.epam.esm.controller.exception.extend;

import com.epam.esm.controller.exception.AbstractServiceException;
import com.epam.esm.controller.exception.ErrorCode;

public class ObjectPostingException extends AbstractServiceException {
    {
        errorCode = ErrorCode.OBJECT_POSTING_ERROR;
    }

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
