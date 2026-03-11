package com.example.digitalassistant.dto;

import java.util.Map;

public record ErrorResponse(
        String message,
        Map<String, String> fieldErrors
) {
}
