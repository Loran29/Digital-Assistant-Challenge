package com.example.digitalassistant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageRequest(
        @NotBlank(message = "message is required")
        @Size(max = 1000, message = "message must not exceed 1000 characters")
        String message
) {
}
