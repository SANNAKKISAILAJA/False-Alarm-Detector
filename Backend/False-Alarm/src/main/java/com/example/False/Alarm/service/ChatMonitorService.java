package com.example.False.Alarm.service;

import com.example.False.Alarm.enums.FlaggedTerms;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class ChatMonitorService {
    private static final Logger logger = LoggerFactory.getLogger(ChatMonitorService.class);

    private final Map<String, Map<String, Integer>> userTermCounts = new HashMap<>();
    private final Map<String, Integer> totalFlaggedUsed = new HashMap<>();
    private final int TERM_LIMIT = 3;
    
    private final AIService aiService;

    @Autowired
    public ChatMonitorService(AIService aiService) {
        this.aiService = aiService;
    }

    public List<String> checkMessage(String userId, String message) {
        List<String> alerts = new ArrayList<>();
        String messageLower = message.toLowerCase();

        userTermCounts.putIfAbsent(userId, new HashMap<>());
        totalFlaggedUsed.putIfAbsent(userId, 0);

        // First check with AI service
        try {
            if (aiService.isMessageToxic(message)) {
                alerts.add("🚫 AI Detection: This message contains potentially harmful content.");
                int total = totalFlaggedUsed.get(userId) + 1;
                totalFlaggedUsed.put(userId, total);
                
                if (total >= TERM_LIMIT) {
                    alerts.add("🚫 You have used flagged terms 3 times. You are now blocked from chatting.");
                    fetchUserLocation(userId);
                }
                return alerts;
            }
        } catch (Exception e) {
            logger.warn("AI service check failed, falling back to basic checks", e);
        }

        // Fallback to basic term checking
        for (String term : FlaggedTerms.getAllTerms()) {
            if (messageLower.contains(term)) {
                Map<String, Integer> termCounts = userTermCounts.get(userId);
                int currentCount = termCounts.getOrDefault(term, 0);
                termCounts.put(term, currentCount + 1);

                int total = totalFlaggedUsed.get(userId) + 1;
                totalFlaggedUsed.put(userId, total);

                if (currentCount == 0) {
                    alerts.add("⚠ Warning: The term \"" + term + "\" should not be used.");
                } else {
                    alerts.add("⚠ The term \"" + term + "\" has been used " + (currentCount + 1) + " time(s).");
                }

                if (total >= TERM_LIMIT) {
                    alerts.add("🚫 You have used flagged terms 3 times. You are now blocked from chatting.");
                    fetchUserLocation(userId);
                }

                break; // Only one term per message
            }
        }

        return alerts.isEmpty() ? List.of("✔ Message accepted.") : alerts;
    }

    private void fetchUserLocation(String userId) {
        System.out.println("Fetching location for user: " + userId);
        // You can integrate with a geolocation service or notify the frontend to request location
    }

    public String resetCounts(String userId) {
        userTermCounts.remove(userId);
        totalFlaggedUsed.remove(userId);
        return "✅ System has been reset. You may chat again.";
    }

    public boolean isBlocked(String userId) {
        return totalFlaggedUsed.getOrDefault(userId, 0) >= TERM_LIMIT;
    }
}
