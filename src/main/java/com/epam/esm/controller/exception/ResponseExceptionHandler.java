package com.epam.esm.controller.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.epam.esm.controller.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private final ErrorResponseBuilder responseBuilder;

    @Autowired
    public ResponseExceptionHandler(ErrorResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    @ExceptionHandler(value = {AbstractServiceException.class})
    protected ResponseEntity<Object> handleServiceException(AbstractServiceException ex, WebRequest request) {
        return handleException(ex, request);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    protected ResponseEntity<Object> handleNullPointerException(RuntimeException ex, WebRequest request) {
        ex.printStackTrace();
        return handleException(ex, INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> handleException(RuntimeException ex, ErrorCode error, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.valueOf(error.getStatusCode());
        ErrorResponse errorResponse = responseBuilder.build(error);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), httpStatus, request);
    }

    private ResponseEntity<Object> handleException(AbstractServiceException ex, WebRequest request) {
        return handleException(ex, ex.getErrorCode(), request);
    }
}