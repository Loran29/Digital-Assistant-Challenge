# Digital Assistant Service

## Challenge Summary
This project implements a simple interview-style REST API where users can:
- Define a digital assistant by `name` and fixed `response`.
- Send a message to an assistant and receive the assistant's fixed response.

Storage is in-memory only (no database), using `ConcurrentHashMap`.

## Tech Stack
- Java 21
- Spring Boot (Maven)
- Spring Web
- Jakarta Validation
- Springdoc OpenAPI (Swagger UI)
- Spring Boot Starter Test (JUnit 5, MockMvc)

## Architecture Overview
Package root: `com.example.digitalassistant`

Layered structure:
- `controller`: REST endpoints and HTTP contract.
- `service`: business logic and in-memory storage.
- `model`: domain model (`Assistant`).
- `dto`: request/response payloads and error payload.
- `exception`: domain exception + global exception handler.

Key implementation details:
- In-memory store: `ConcurrentHashMap<String, Assistant>`
- Upsert behavior:
  - New assistant: `201 Created`
  - Existing assistant updated: `200 OK`
- Global structured error responses for validation and not-found cases.

## Run Instructions
### Prerequisites
- Java 21 installed and available in `PATH` (or set `JAVA_HOME`)

### Start the service
From project root:

```bash
./mvnw spring-boot:run
```

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

App default URL:
- `http://localhost:8080`

Swagger UI:
- `http://localhost:8080/swagger-ui.html`

OpenAPI JSON:
- `http://localhost:8080/api-docs`

### Run tests
```bash
./mvnw test
```

Windows PowerShell:
```powershell
.\mvnw.cmd test
```

## API Endpoints

### 1. Create or update assistant
`POST /api/assistants`

Request:
```json
{
  "name": "finance-bot",
  "response": "Hello, I am your finance assistant."
}
```

Responses:
- `201 Created` if assistant is created
- `200 OK` if assistant is updated

### 2. Get assistant by name
`GET /api/assistants/{name}`

Response:
- `200 OK` with assistant details
- `404 Not Found` if not found

### 3. Send message to assistant
`POST /api/assistants/{name}/messages`

Request:
```json
{
  "message": "Hi"
}
```

Response:
```json
{
  "assistantName": "finance-bot",
  "inputMessage": "Hi",
  "response": "Hello, I am your finance assistant."
}
```

Response codes:
- `200 OK`
- `404 Not Found` if assistant not found

## Validation Rules
- `assistant.name`: not blank, 2-50 chars, letters/numbers/hyphen/underscore only
- `assistant.response`: not blank, max 500 chars
- `message`: not blank, max 1000 chars

## Error Handling
Global error handling is intentionally simple:
- `AssistantNotFoundException` -> `404 Not Found`
- `MethodArgumentNotValidException` or `ConstraintViolationException` -> `400 Bad Request`

Validation error example (`400`):
```json
{
  "message": "Validation failed",
  "fieldErrors": {}
}
```

Assistant not found example (`404`):
```json
{
  "message": "Assistant not found: missing-bot",
  "fieldErrors": {}
}
```

## Assumptions
- Assistant names are unique keys and case-sensitive.
- Data is in-memory only and resets on application restart.
- Upsert is intentionally implemented through `POST /api/assistants` per challenge requirements.
