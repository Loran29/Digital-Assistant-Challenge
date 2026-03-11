package com.example.digitalassistant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AssistantUpsertRequest(
        @NotBlank(message = "name is required")
        @Pattern(
                regexp = NAME_PATTERN,
                message = "name must be 2-50 chars and contain only letters, numbers, hyphen, or underscore"
        )
        String name,
        @NotBlank(message = "response is required")
        @Size(max = 500, message = "response must not exceed 500 characters")
        String response
) {
    public static final String NAME_PATTERN = "^[A-Za-z0-9_-]{2,50}$";
}
