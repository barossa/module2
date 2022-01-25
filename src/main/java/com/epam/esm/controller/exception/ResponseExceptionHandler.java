package com.epam.esm.controller.exception;

import com.epam.esm.controller.exception.extend.ObjectValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import static com.epam.esm.controller.exception.ErrorCode.*;

@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LogManager.getLogger(ResponseExceptionHandler.class);

    private final ErrorResponseBuilder responseBuilder;

    @Autowired
    public ResponseExceptionHandler(ErrorResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handle(BAD_REQUEST_PARAMETERS);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handle(BAD_REQUEST_PARAMETERS);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handle(BAD_REQUEST_PARAMETERS);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handle(METHOD_NOT_SUPPORTED);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    protected ResponseEntity<Object> handleNullPointerException(RuntimeException ex) {
        ex.printStackTrace();
        return handle(INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {AbstractServiceException.class})
    protected ResponseEntity<Object> handleServiceException(AbstractServiceException ex) {
        return handle(ex.getErrorCode());
    }

    @ExceptionHandler(value = {ObjectValidationException.class})
    protected ResponseEntity<Object> handleValidationException(ObjectValidationException ex) {
        ErrorCode error = ex.errorCode;
        ErrorResponse errorResponse = responseBuilder.build(error);
        errorResponse.setDescription(ex.getErrors());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(error.getStatusCode()));
    }

    private ResponseEntity<Object> handle(ErrorCode error) {
        HttpStatus httpStatus = HttpStatus.valueOf(error.getStatusCode());
        ErrorResponse errorResponse = responseBuilder.build(error);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.warn("Caught unknown internal exception: {}: {}.", ex.getClass(), ex.getMessage());
        return handle(ErrorCode.UNKNOWN_INTERNAL_ERROR);
    }
}