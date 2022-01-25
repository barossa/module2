package com.epam.esm.controller.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class NotFoundHandler implements HttpRequestHandler {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_CONTENT_TYPE = "application/json";

    private final ErrorResponseBuilder errorResponseBuilder;
    private final ObjectMapper objectMapper;

    public NotFoundHandler(ErrorResponseBuilder errorResponseBuilder, ObjectMapper objectMapper) {
        this.errorResponseBuilder = errorResponseBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ErrorResponse errorResponse = errorResponseBuilder.build(ErrorCode.PATH_NOT_FOUND);
        ObjectWriter objectWriter = objectMapper.writer();
        String responseBody = objectWriter.writeValueAsString(errorResponse);
        response.setHeader(CONTENT_TYPE_HEADER, JSON_CONTENT_TYPE);
        PrintWriter writer = response.getWriter();
        writer.write(responseBody);
    }
}
