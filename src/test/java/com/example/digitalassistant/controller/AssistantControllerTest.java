package com.example.digitalassistant.controller;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.digitalassistant.exception.AssistantNotFoundException;
import com.example.digitalassistant.exception.GlobalExceptionHandler;
import com.example.digitalassistant.service.InMemoryAssistantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;

@WebMvcTest(AssistantController.class)
@Import(GlobalExceptionHandler.class)
class AssistantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InMemoryAssistantService assistantService;

    @Test
    void shouldCreateAssistant() throws Exception {
        String assistantName = "finance-bot";
        String assistantResponse = "Hello, I am your finance assistant.";

        given(assistantService.upsertAssistant(assistantName, assistantResponse))
                .willReturn(true);

        String payload = """
                {
                  "name": "finance-bot",
                  "response": "Hello, I am your finance assistant."
                }
                """;

        mockMvc.perform(post("/api/assistants")
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/assistants/finance-bot")))
                .andExpect(jsonPath("$.name").value("finance-bot"))
                .andExpect(jsonPath("$.response").value("Hello, I am your finance assistant."));
    }

    @Test
    void shouldReturnBadRequestOnValidationFailure() throws Exception {
        String payload = """
                {
                  "name": " ",
                  "response": "Hello"
                }
                """;

        mockMvc.perform(post("/api/assistants")
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }

    @Test
    void shouldReturnNotFoundWhenSendingMessageToUnknownAssistant() throws Exception {
        given(assistantService.sendMessage("missing-bot", "Hi"))
                .willThrow(new AssistantNotFoundException("missing-bot"));

        String payload = """
                {
                  "message": "Hi"
                }
                """;

        mockMvc.perform(post("/api/assistants/missing-bot/messages")
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Assistant not found: missing-bot"));
    }
}
