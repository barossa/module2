package com.epam.esm.controller;

import com.epam.esm.controller.dto.ErrorCode;
import com.epam.esm.controller.dto.ErrorResponse;
import com.epam.esm.controller.dto.ErrorResponseBuilder;
import com.epam.esm.service.exception.AbstractServiceException;
import com.epam.esm.service.exception.extend.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static com.epam.esm.controller.dto.ErrorCode.*;

@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LogManager.getLogger(ResponseExceptionHandler.class);

    private static final Map<Class<? extends Exception>, ErrorCode> exceptions = new HashMap<>();

    static {
        exceptions.put(TypeMismatchException.class, BAD_REQUEST_PARAMETERS);
        exceptions.put(MethodArgumentNotValidException.class, BAD_REQUEST_PARAMETERS);
        exceptions.put(MethodArgumentTypeMismatchException.class, BAD_REQUEST_PARAMETERS);
        exceptions.put(MethodArgumentConversionNotSupportedException.class, BAD_REQUEST_PARAMETERS);
        exceptions.put(BindException.class, BAD_REQUEST_PARAMETERS);
        exceptions.put(MissingPathVariableException.class, BAD_REQUEST_PARAMETERS);
        exceptions.put(MissingServletRequestParameterException.class, BAD_REQUEST_PARAMETERS);
        exceptions.put(HttpRequestMethodNotSupportedException.class, METHOD_NOT_SUPPORTED);
        exceptions.put(NoHandlerFoundException.class, PATH_NOT_FOUND);
        exceptions.put(HttpMediaTypeNotAcceptableException.class, CONTENT_TYPE_NOT_ACCEPTABLE);

        exceptions.put(DataAccessException.class, DATA_ACCESS_ERROR);
        exceptions.put(EmptySearchRequestException.class, EMPTY_SEARCH_REQUEST);
        exceptions.put(LongSearchRequestException.class, LONG_SEARCH_REQUEST);
        exceptions.put(ObjectAlreadyExistsException.class, OBJECT_ALREADY_EXISTS);
        exceptions.put(ObjectNotFoundException.class, OBJECT_NOT_FOUND);
        exceptions.put(ObjectNotPresentedForUpdateException.class, OBJECT_NOT_PRESENTED_FOR_UPDATE);
        exceptions.put(ObjectPostingException.class, OBJECT_POSTING_ERROR);
        exceptions.put(InvalidSortParametersException.class, INVALID_SORT_PARAMETERS);
    }

    private final ErrorResponseBuilder responseBuilder;

    @Autowired
    public ResponseExceptionHandler(ErrorResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    @ExceptionHandler(value = {NullPointerException.class})
    protected ResponseEntity<Object> handleNullPointerException(RuntimeException ex) {
        logger.error(ex);
        return handle(INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ObjectValidationException.class})
    protected ResponseEntity<Object> handleValidationException(ObjectValidationException ex) {
        ErrorResponse errorResponse = responseBuilder.build(OBJECT_VALIDATION_ERROR);
        errorResponse.setDescription(ex.getErrors());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(OBJECT_VALIDATION_ERROR.getStatusCode()));
    }

    @ExceptionHandler(value = AbstractServiceException.class)
    protected ResponseEntity<Object> handleException(Exception ex) {
        ErrorCode errorCode = exceptions.get(ex.getClass());
        if (errorCode != null) {
            return handle(errorCode);
        }
        logger.warn("Caught unknown internal exception: {}: {}.", ex.getClass(), ex.getMessage());
        return handle(ErrorCode.UNKNOWN_INTERNAL_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleException(ex);
    }

    private ResponseEntity<Object> handle(ErrorCode error) {
        HttpStatus httpStatus = HttpStatus.valueOf(error.getStatusCode());
        ErrorResponse errorResponse = responseBuilder.build(error);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}