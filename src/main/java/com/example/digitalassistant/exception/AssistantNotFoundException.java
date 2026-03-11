package com.example.digitalassistant.exception;

public class AssistantNotFoundException extends RuntimeException {

    public AssistantNotFoundException(String name) {
        super("Assistant not found: " + name);
    }
}
