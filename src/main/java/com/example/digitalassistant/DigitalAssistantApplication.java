package com.example.digitalassistant;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Digital Assistant Service API",
                version = "v1",
                description = "Simple in-memory API for defining assistants and sending messages"
        )
)
public class DigitalAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalAssistantApplication.class, args);
    }
}
