package com.epam.esm.controller.error;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int errorCode;
    private String message;
}
