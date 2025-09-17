package com.cknoe.backend_springboot.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        Map<String, String> fieldErrors,
        String path,
        LocalDateTime timestamp) {
    public static ValidationErrorResponse of(Map<String, String> fieldErrors, String path) {
        return new ValidationErrorResponse(
                fieldErrors,
                path,
                LocalDateTime.now());
    }
}