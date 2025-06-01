package com.example.False.Alarm.service;

import com.example.False.Alarm.dto.AIResponse;
import com.example.False.Alarm.dto.TextInput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AIService {
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    private final RestTemplate restTemplate;
    private final String aiServiceUrl;

    public AIService(@Value("${ai-service.url:http://localhost:8000}") String aiServiceUrl) {
        this.restTemplate = new RestTemplate();
        this.aiServiceUrl = aiServiceUrl;
    }

    public boolean isMessageToxic(String message) {
        try {
            TextInput input = new TextInput();
            input.setText(message);

            ResponseEntity<AIResponse> response = restTemplate.postForEntity(
                aiServiceUrl + "/predict",
                input,
                AIResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                AIResponse aiResponse = response.getBody();
                // Check if any prediction indicates toxic content with high confidence
                return aiResponse.getPredictions().stream()
                    .anyMatch(pred -> pred.getLabel().equals("toxic") && pred.getScore() > 0.7);
            }
        } catch (Exception e) {
            logger.error("Error calling AI service: ", e);
            // If AI service fails, fall back to basic checks
            return false;
        }
        return false;
    }
}
