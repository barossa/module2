package com.epam.esm.controller.exception.extend;

import com.epam.esm.controller.exception.AbstractServiceException;
import com.epam.esm.controller.exception.ErrorCode;

public class LongSearchRequestException extends AbstractServiceException {
    {
        errorCode = ErrorCode.LONG_SEARCH_REQUEST;
    }

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
