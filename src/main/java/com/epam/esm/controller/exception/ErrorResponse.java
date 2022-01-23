package com.epam.esm.controller.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
class ErrorResponse {
    private LocalDateTime timestamp;

    private int errorCode;

    private String message;

    private List<String> description;
}
