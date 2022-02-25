package com.epam.esm.exception.extend;

import com.epam.esm.exception.AbstractServiceException;

public class InvalidSortParametersException extends AbstractServiceException {
    public InvalidSortParametersException() {
    }

    public InvalidSortParametersException(String message) {
        super(message);
    }

    public InvalidSortParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSortParametersException(Throwable cause) {
        super(cause);
    }

    public InvalidSortParametersException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
