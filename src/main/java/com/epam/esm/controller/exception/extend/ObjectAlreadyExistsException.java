package com.epam.esm.controller.exception.extend;

import com.epam.esm.controller.exception.AbstractServiceException;
import com.epam.esm.controller.exception.ErrorCode;

public class ObjectAlreadyExistsException extends AbstractServiceException {
    {
        errorCode = ErrorCode.OBJECT_ALREADY_EXISTS;
    }

    public ObjectAlreadyExistsException() {
    }

    public ObjectAlreadyExistsException(String message) {
        super(message);
    }

    public ObjectAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ObjectAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
