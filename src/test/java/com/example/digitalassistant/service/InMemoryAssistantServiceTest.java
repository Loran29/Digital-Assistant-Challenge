package com.example.digitalassistant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.digitalassistant.dto.MessageResponse;
import com.example.digitalassistant.exception.AssistantNotFoundException;
import com.example.digitalassistant.model.Assistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryAssistantServiceTest {

    private InMemoryAssistantService assistantService;

    @BeforeEach
    void setUp() {
        assistantService = new InMemoryAssistantService();
    }

    @Test
    void shouldCreateGetUpdateAndSendMessage() {
        boolean created = assistantService.upsertAssistant(
                "finance-bot",
                "Hello, I am your finance assistant."
        );

        assertTrue(created);

        Assistant fetched = assistantService.getAssistant("finance-bot");
        assertEquals("finance-bot", fetched.name());
        assertEquals("Hello, I am your finance assistant.", fetched.response());

        boolean updated = assistantService.upsertAssistant(
                "finance-bot",
                "Updated fixed response."
        );

        assertFalse(updated);
        assertEquals("Updated fixed response.", assistantService.getAssistant("finance-bot").response());

        MessageResponse messageResponse = assistantService.sendMessage("finance-bot", "Hi");
        assertEquals("finance-bot", messageResponse.assistantName());
        assertEquals("Hi", messageResponse.inputMessage());
        assertEquals("Updated fixed response.", messageResponse.response());
    }

    @Test
    void shouldThrowWhenAssistantDoesNotExist() {
        assertThrows(AssistantNotFoundException.class, () -> assistantService.getAssistant("missing-bot"));
        assertThrows(
                AssistantNotFoundException.class,
                () -> assistantService.sendMessage("missing-bot", "Hello")
        );
    }
}
