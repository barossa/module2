package com.epam.esm.exception.extend;

import com.epam.esm.exception.AbstractServiceException;

public class CertificateNotFoundException extends AbstractServiceException {
    public CertificateNotFoundException() {
        super();
    }

    public CertificateNotFoundException(String message) {
        super(message);
    }

    public CertificateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificateNotFoundException(Throwable cause) {
        super(cause);
    }

    public CertificateNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
