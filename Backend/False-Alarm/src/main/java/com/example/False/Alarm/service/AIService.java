package com.example.False.Alarm.service;

import com.example.False.Alarm.dto.AIResponse;
import com.example.False.Alarm.dto.TextInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AIService {
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    private final RestTemplate restTemplate;
    private final String aiServiceUrl;
    private final String aiServiceEndpoint;
    private final int maxRetries = 3;
    private final long retryDelayMs = 1000;

    @Autowired
    public AIService(@Value("${ai-service.url:http://localhost:8000}") String aiServiceUrl,
                     @Value("${ai-service.endpoint:/predict}") String aiServiceEndpoint, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.aiServiceUrl = aiServiceUrl;
        this.aiServiceEndpoint = aiServiceEndpoint;
    }

    public boolean isMessageToxic(String message) {
        if (restTemplate == null || message == null) {
            logger.warn("AI service not properly initialized or message is null");
            return false;
        }

        String url = aiServiceUrl + aiServiceEndpoint;
        logger.debug("Calling AI service at: {} with message: {}", url, message);

        try {
            TextInput input = new TextInput(message);
            for (int i = 0; i < maxRetries; i++) {
                try {
                    AIResponse response = restTemplate.postForObject(url, input, AIResponse.class);
                    if (response != null && response.isToxic()) {
                        logger.info("Message flagged as toxic: {}", message);
                        return true;
                    }
                    return false;
                } catch (ResourceAccessException e) {
                    if (i == maxRetries - 1) {
                        logger.error("Failed to connect to AI service after {} retries", maxRetries, e);
                        throw e;
                    }
                    logger.warn("Failed to connect to AI service, retrying in {}ms", retryDelayMs);
                    Thread.sleep(retryDelayMs);
                }
            }
        } catch (Exception e) {
            logger.error("Error processing message with AI service", e);
            return false;
        }
        return false;

    }
}
