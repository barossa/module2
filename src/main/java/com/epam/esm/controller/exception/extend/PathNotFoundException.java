package com.epam.esm.controller.exception.extend;

import com.epam.esm.controller.exception.AbstractServiceException;
import com.epam.esm.controller.exception.ErrorCode;

public class PathNotFoundException extends AbstractServiceException {
    {
        errorCode = ErrorCode.PATH_NOT_FOUND;
    }

    public PathNotFoundException() {
    }

    public PathNotFoundException(String message) {
        super(message);
    }

    public PathNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PathNotFoundException(Throwable cause) {
        super(cause);
    }

    public PathNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
