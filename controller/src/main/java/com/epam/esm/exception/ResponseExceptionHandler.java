package com.epam.esm.exception;

import com.epam.esm.exception.extend.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
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

@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LogManager.getLogger(ResponseExceptionHandler.class);

    private static final Map<Class<? extends Exception>, ErrorCode> exceptions = new HashMap<>();

    static {
        exceptions.put(TypeMismatchException.class, ErrorCode.BAD_REQUEST_PARAMETERS);
        exceptions.put(MethodArgumentNotValidException.class, ErrorCode.BAD_REQUEST_PARAMETERS);
        exceptions.put(MethodArgumentTypeMismatchException.class, ErrorCode.BAD_REQUEST_PARAMETERS);
        exceptions.put(MethodArgumentConversionNotSupportedException.class, ErrorCode.BAD_REQUEST_PARAMETERS);
        exceptions.put(BindException.class, ErrorCode.BAD_REQUEST_PARAMETERS);
        exceptions.put(MissingPathVariableException.class, ErrorCode.BAD_REQUEST_PARAMETERS);
        exceptions.put(MissingServletRequestParameterException.class, ErrorCode.BAD_REQUEST_PARAMETERS);
        exceptions.put(HttpRequestMethodNotSupportedException.class, ErrorCode.METHOD_NOT_SUPPORTED);
        exceptions.put(NoHandlerFoundException.class, ErrorCode.PATH_NOT_FOUND);
        exceptions.put(HttpMediaTypeNotAcceptableException.class, ErrorCode.CONTENT_TYPE_NOT_ACCEPTABLE);

        exceptions.put(DataAccessException.class, ErrorCode.DATA_ACCESS_ERROR);
        exceptions.put(EmptySearchRequestException.class, ErrorCode.EMPTY_SEARCH_REQUEST);
        exceptions.put(LongSearchRequestException.class, ErrorCode.LONG_SEARCH_REQUEST);
        exceptions.put(ObjectAlreadyExistsException.class, ErrorCode.OBJECT_ALREADY_EXISTS);
        exceptions.put(ObjectNotPresentedForUpdateException.class, ErrorCode.OBJECT_NOT_PRESENTED_FOR_UPDATE);
        exceptions.put(ObjectPostingException.class, ErrorCode.OBJECT_POSTING_ERROR);
        exceptions.put(InvalidSortParametersException.class, ErrorCode.INVALID_SORT_PARAMETERS);
        exceptions.put(ObjectNotPresentedForDelete.class, ErrorCode.OBJECT_NOT_PRESENTED_FOR_DELETE);
        exceptions.put(ObjectDeletionException.class, ErrorCode.OBJECT_DELETION_ERROR);

        exceptions.put(TagNotFoundException.class, ErrorCode.TAG_NOT_FOUND);
        exceptions.put(CertificateNotFoundException.class, ErrorCode.CERTIFICATE_NOT_FOUND);
        exceptions.put(UserNotFoundException.class, ErrorCode.USER_NOT_FOUND);
        exceptions.put(OrderNotFoundException.class, ErrorCode.ORDER_NOT_FOUND);
    }

    private final ErrorResponseBuilder responseBuilder;

    @ExceptionHandler(value = {NullPointerException.class})
    protected ResponseEntity<Object> handleNullPointerException(RuntimeException ex) {
        logger.error(ex);
        return handle(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ObjectValidationException.class})
    protected ResponseEntity<Object> handleValidationException(ObjectValidationException ex) {
        ErrorResponse errorResponse = responseBuilder.build(ErrorCode.OBJECT_VALIDATION_ERROR);
        errorResponse.setDescription(ex.getErrors());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ErrorCode.OBJECT_VALIDATION_ERROR.getStatusCode()));
    }

    @ExceptionHandler(value = AbstractServiceException.class)
    protected ResponseEntity<Object> handleException(Exception ex) {
        ErrorCode errorCode = exceptions.get(ex.getClass());
        if (errorCode == null) {
            errorCode = ErrorCode.UNKNOWN_INTERNAL_ERROR;
            logger.warn("Caught unknown internal exception: {}: {}.", ex.getClass(), ex.getMessage());
        }
        return handle(errorCode);
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