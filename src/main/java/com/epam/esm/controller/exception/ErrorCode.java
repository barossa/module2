package com.epam.esm.controller.exception;

public enum ErrorCode {
    OBJECT_NOT_FOUND(40401, 404),
    OBJECT_NOT_PRESENTED_FOR_UPDATE(40402,404),
    INTERNAL_SERVER_ERROR(50001, 500),
    OBJECT_POSTING_ERROR(50002, 500),
    DATA_ACCESS_ERROR(50003, 500),
    OBJECT_ALREADY_EXISTS(40901, 409),
    METHOD_NOT_SUPPORTED(40501,405);

    private final int errorCode;

    private final int statusCode;

    ErrorCode(int errorCode, int statusCode) {
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return String.valueOf(errorCode);
    }
}
