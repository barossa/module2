package com.epam.esm.controller;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.ErrorResponse;
import com.epam.esm.exception.ErrorResponseBuilder;
import com.epam.esm.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import static com.epam.esm.exception.ErrorCode.INVALID_TOKEN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class SecurityController {

    private final UserDetailsService userDetailsService;
    private final ErrorResponseBuilder errorBuilder;

    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshToken(@RequestHeader(name = AUTHORIZATION) String authorization) {
        try {
            String refreshToken = JwtUtils.matchToken(authorization);
            JWTVerifier verifier = JwtUtils.getVerifier();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            UserDto user = (UserDto) userDetailsService.loadUserByUsername(decodedJWT.getSubject());
            String accessToken = JwtUtils.buildAccessToken(user, refreshToken);
            return ResponseEntity.ok(Collections.singletonMap("access_token", accessToken));

        } catch (Exception e) {
            ErrorResponse errorResponse = errorBuilder.build(INVALID_TOKEN);
            return ResponseEntity.status(INVALID_TOKEN.getStatusCode()).body(errorResponse);
        }
    }
}
