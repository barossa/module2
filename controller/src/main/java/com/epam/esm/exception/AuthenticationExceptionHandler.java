package com.epam.esm.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.epam.esm.exception.ErrorCode.ACCESS_DENIED;
import static com.epam.esm.exception.ErrorCode.BAD_CREDENTIALS;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private static final Map<Class<? extends AuthenticationException>, ErrorCode> exceptions = new HashMap<>();

    static {
        exceptions.put(BadCredentialsException.class, BAD_CREDENTIALS);
        exceptions.put(InsufficientAuthenticationException.class, ACCESS_DENIED);
        exceptions.put(AuthenticationException.class, ACCESS_DENIED);
    }

    private final ErrorResponseBuilder errorBuilder;
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        handleException(request, response, authException);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        handleException(request, response, accessDeniedException);
    }

    protected void handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        Class<? extends Exception> exceptionClass = ex.getClass();
        ErrorCode errorCode = exceptions.get(exceptionClass);
        if (errorCode == null) {
            errorCode = ACCESS_DENIED;
        }
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getStatusCode());
        ErrorResponse errorResponse = errorBuilder.build(errorCode);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
