package com.epam.esm.controller.exception.extend;

import com.epam.esm.controller.exception.AbstractServiceException;
import com.epam.esm.controller.exception.ErrorCode;

public class EmptySearchRequestException extends AbstractServiceException {
    {
        errorCode = ErrorCode.EMPTY_SEARCH_REQUEST;
    }
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
