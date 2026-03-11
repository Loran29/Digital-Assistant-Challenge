package com.example.digitalassistant.controller;

import com.example.digitalassistant.dto.AssistantResponse;
import com.example.digitalassistant.dto.AssistantUpsertRequest;
import com.example.digitalassistant.dto.MessageRequest;
import com.example.digitalassistant.dto.MessageResponse;
import com.example.digitalassistant.model.Assistant;
import com.example.digitalassistant.service.InMemoryAssistantService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Validated
@RequestMapping("/api/assistants")
public class AssistantController {

    private static final String NAME_VALIDATION_MESSAGE =
            "name must be 2-50 chars and contain only letters, numbers, hyphen, or underscore";

    private final InMemoryAssistantService assistantService;

    public AssistantController(InMemoryAssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @PostMapping
    public ResponseEntity<AssistantResponse> upsertAssistant(@Valid @RequestBody AssistantUpsertRequest request) {
        boolean created = assistantService.upsertAssistant(request.name(), request.response());
        AssistantResponse response = new AssistantResponse(request.name(), request.response());

        if (created) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{name}")
                    .buildAndExpand(response.name())
                    .toUri();
            return ResponseEntity.created(location).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{name}")
    public AssistantResponse getAssistant(
            @PathVariable
            @Pattern(
                    regexp = AssistantUpsertRequest.NAME_PATTERN,
                    message = NAME_VALIDATION_MESSAGE
            )
            String name
    ) {
        Assistant assistant = assistantService.getAssistant(name);
        return new AssistantResponse(assistant.name(), assistant.response());
    }

    @PostMapping("/{name}/messages")
    public MessageResponse sendMessage(
            @PathVariable
            @Pattern(
                    regexp = AssistantUpsertRequest.NAME_PATTERN,
                    message = NAME_VALIDATION_MESSAGE
            )
            String name,
            @Valid @RequestBody MessageRequest request
    ) {
        return assistantService.sendMessage(name, request.message());
    }
}
