package com.epam.esm.controller.exception;

import com.epam.esm.controller.exception.extend.ObjectValidationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handle(ex, ErrorCode.BAD_REQUEST_PARAMETERS, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handle(ex, ErrorCode.BAD_REQUEST_PARAMETERS, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handle(ex, ErrorCode.BAD_REQUEST_PARAMETERS, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handle(ex, ErrorCode.METHOD_NOT_SUPPORTED, request);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    protected ResponseEntity<Object> handleNullPointerException(RuntimeException ex, WebRequest request) {
        return handle(ex, INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {AbstractServiceException.class})
    protected ResponseEntity<Object> handleServiceException(AbstractServiceException ex, WebRequest request) {
        return handle(ex, ex.getErrorCode(), request);
    }

    @ExceptionHandler(value = {ObjectValidationException.class})
    protected ResponseEntity<Object> handleValidationException(ObjectValidationException ex, WebRequest request) {
        ErrorCode error = ex.errorCode;
        HttpStatus httpStatus = HttpStatus.valueOf(error.getStatusCode());
        ErrorResponse errorResponse = responseBuilder.build(error);
        errorResponse.setDescription(ex.getErrors());
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), httpStatus, request);
    }

    private ResponseEntity<Object> handle(Exception ex, ErrorCode error, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.valueOf(error.getStatusCode());
        ErrorResponse errorResponse = responseBuilder.build(error);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), httpStatus, request);
    }
}