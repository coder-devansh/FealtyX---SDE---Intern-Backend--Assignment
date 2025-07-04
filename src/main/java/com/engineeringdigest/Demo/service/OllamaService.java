package com.engineeringdigest.Demo.service;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class OllamaService {

    private static final String OLLAMA_URL = "http://localhost:11434/api/chat";
    private static final String MODEL_NAME = "Llama 3.1"; // or llama2, gemma, etc.

    public String generateSummary(String studentInfo) {
    RestTemplate restTemplate = new RestTemplate();

    String url = "http://localhost:11434/api/chat";

    Map<String, Object> message = Map.of(
        "role", "user",
        "content", studentInfo
    );

    Map<String, Object> body = Map.of(
        "model", "llama3",
        "messages", List.of(message),
        "stream", false
    );

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

    try {
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        System.out.println("Ollama response: " + response);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().get("message").toString();  // sometimes wrapped in `message.content`
        } else {
            throw new RuntimeException("Error from Ollama API");
        }

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Ollama call failed: " + e.getMessage());
    }
}
}
