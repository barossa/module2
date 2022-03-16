package com.epam.esm.exception;

public enum ErrorCode {
    BAD_CREDENTIALS(40101, 401),
    INVALID_TOKEN(40102, 401),
    ACCESS_DENIED(40301,403),
    TAG_NOT_FOUND(40401, 404),
    OBJECT_NOT_PRESENTED_FOR_UPDATE(40402, 404),
    PATH_NOT_FOUND(40403, 404),
    OBJECT_NOT_PRESENTED_FOR_DELETE(40404,404),
    CERTIFICATE_NOT_FOUND(40405,404),
    USER_NOT_FOUND(40406,404),
    ORDER_NOT_FOUND(40407,404),
    INTERNAL_SERVER_ERROR(50001, 500),
    OBJECT_POSTING_ERROR(50002, 500),
    DATA_ACCESS_ERROR(50003, 500),
    UNKNOWN_INTERNAL_ERROR(50004, 500),
    OBJECT_DELETION_ERROR(50005, 500),
    OBJECT_ALREADY_EXISTS(40901, 409),
    METHOD_NOT_SUPPORTED(40501, 405),
    CONTENT_TYPE_NOT_ACCEPTABLE(40601,406),
    OBJECT_VALIDATION_ERROR(40001, 400),
    BAD_REQUEST_PARAMETERS(40002, 400),
    LONG_SEARCH_REQUEST(40003, 400),
    EMPTY_SEARCH_REQUEST(40004, 400),
    INVALID_SORT_PARAMETERS(40005,400);

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
