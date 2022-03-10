package com.epam.esm.filter;

import com.epam.esm.dto.AuthenticationRequest;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.ErrorResponse;
import com.epam.esm.exception.ErrorResponseBuilder;
import com.epam.esm.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.epam.esm.exception.ErrorCode.BAD_CREDENTIALS;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final ErrorResponseBuilder errorBuilder;

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        UserDto user = (UserDto) authResult.getPrincipal();
        String issuer = request.getRequestURL().toString();
        Map<String, String> tokens = JwtUtils.buildTokens(user, issuer);
        response.setHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(BAD_CREDENTIALS.getStatusCode());
        ErrorResponse errorResponse = errorBuilder.build(BAD_CREDENTIALS);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthenticationRequest authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), AuthenticationRequest.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            return authenticationManager.authenticate(authenticationToken);

        } catch (Exception e) {
            throw new BadCredentialsException("Can't properly read authentication credentials", e);
        }
    }


}
