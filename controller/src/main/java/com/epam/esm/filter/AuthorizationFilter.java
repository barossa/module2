package com.epam.esm.filter;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.esm.exception.ErrorResponse;
import com.epam.esm.exception.ErrorResponseBuilder;
import com.epam.esm.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.esm.exception.ErrorCode.INVALID_TOKEN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final ErrorResponseBuilder errorBuilder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getServletPath().equals("/token/authenticate")) {
            String header = request.getHeader(AUTHORIZATION);
            String token = JwtUtils.matchToken(header);
            if (token != null) {
                try {
                    JWTVerifier verifier = JwtUtils.getVerifier();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    UsernamePasswordAuthenticationToken authenticationToken = JwtUtils.parseJwt(decodedJWT);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                } catch (Exception e) {
                    ErrorResponse errorResponse = errorBuilder.build(INVALID_TOKEN);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setStatus(INVALID_TOKEN.getStatusCode());
                    objectMapper.writeValue(response.getOutputStream(), errorResponse);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
