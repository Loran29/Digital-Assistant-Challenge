package com.example.digitalassistant.service;

import com.example.digitalassistant.dto.MessageResponse;
import com.example.digitalassistant.exception.AssistantNotFoundException;
import com.example.digitalassistant.model.Assistant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;

@Service
public class InMemoryAssistantService {

    private final ConcurrentMap<String, Assistant> assistants = new ConcurrentHashMap<>();

    public boolean upsertAssistant(String name, String response) {
        Assistant assistant = new Assistant(name, response);
        Assistant previous = assistants.put(name, assistant);
        return previous == null;
    }

    public Assistant getAssistant(String name) {
        Assistant assistant = assistants.get(name);
        if (assistant == null) {
            throw new AssistantNotFoundException(name);
        }
        return assistant;
    }

    public MessageResponse sendMessage(String name, String message) {
        Assistant assistant = getAssistant(name);
        return new MessageResponse(assistant.name(), message, assistant.response());
    }
}
