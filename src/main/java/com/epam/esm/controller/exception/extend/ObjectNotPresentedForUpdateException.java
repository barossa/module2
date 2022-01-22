package com.epam.esm.controller.exception.extend;

import com.epam.esm.controller.exception.AbstractServiceException;
import com.epam.esm.controller.exception.ErrorCode;

public class ObjectNotPresentedForUpdateException extends AbstractServiceException {
    {
        errorCode = ErrorCode.OBJECT_NOT_PRESENTED_FOR_UPDATE;
    }

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
