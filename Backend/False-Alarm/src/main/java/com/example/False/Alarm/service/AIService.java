package com.example.False.Alarm.service;

import com.example.False.Alarm.dto.AIResponse;
import com.example.False.Alarm.dto.TextInput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AIService {
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    private final RestTemplate restTemplate;
    @Value("${ai-service.url:http://localhost:8000}")
    private String aiServiceUrl;
    @Value("${ai-service.endpoint:/predict}")
    private String aiServiceEndpoint;

    public AIService(@Value("${ai-service.url:http://localhost:8000}") String aiServiceUrl) {
        this.restTemplate = new RestTemplate();
        this.aiServiceUrl = aiServiceUrl;
    }

    public boolean isMessageToxic(String message) {
        try {
            TextInput input = new TextInput(message);
            AIResponse response = restTemplate.postForObject(
                aiServiceUrl + aiServiceEndpoint,
                input,
                AIResponse.class
            );
            return response != null && response.isToxic();
        } catch (Exception e) {
            logger.error("AI service unavailable. Falling back to basic checks.", e);
            String lowerCaseMessage = message.toLowerCase();
            return lowerCaseMessage.contains("abuse") ||
                   lowerCaseMessage.contains("hate") ||
                   lowerCaseMessage.contains("harassment") ||
                   lowerCaseMessage.contains("violence");
        }
    }
}
