package com.example.False.Alarm.service;

import com.example.False.Alarm.enums.FlaggedTerms;
import com.example.False.Alarm.model.FlaggedUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatMonitorService {
    private static final Logger logger = LoggerFactory.getLogger(ChatMonitorService.class);

    private final Map<String, Map<String, Integer>> userTermCounts = new ConcurrentHashMap<>();
    private final Map<String, Integer> totalFlaggedUsed = new ConcurrentHashMap<>();
    private final Map<String, FlaggedUserDetails> flaggedUserDetails = new ConcurrentHashMap<>();
    private final int TERM_LIMIT = 3;

    private final AIService aiService;

    @Autowired
    public ChatMonitorService(AIService aiService) {
        this.aiService = aiService;
    }

    public List<String> checkMessage(String userId, String username, String message, String location) {
        List<String> alerts = new ArrayList<>();
        String messageLower = message.toLowerCase();

        userTermCounts.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        totalFlaggedUsed.putIfAbsent(userId, 0);

        // AI check
        try {
            if (aiService.isMessageToxic(message)) {
                alerts.add("🚫 AI Detection: This message contains potentially harmful content.");
                incrementAndBlock(userId, username, message, location, alerts);
                return alerts;
            }
        } catch (Exception e) {
            logger.warn("AI service check failed, falling back to basic checks", e);
        }

        // Basic term check
        for (String term : FlaggedTerms.getAllTerms()) {
            if (messageLower.contains(term)) {
                processTerm(userId, username, term, message, location, alerts);
                break; // Only one term per message (remove if you want all)
            }
        }

        return alerts.isEmpty() ? List.of("✔ Message accepted.") : alerts;
    }

    private void processTerm(String userId, String username, String term, String message, String location, List<String> alerts) {
        Map<String, Integer> termCounts = userTermCounts.get(userId);
        int count = termCounts.getOrDefault(term, 0);
        termCounts.put(term, count + 1);

        alerts.add(count == 0
            ? "⚠ Warning: The term \"" + term + "\" should not be used."
            : "⚠ The term \"" + term + "\" has been used " + (count + 1) + " time(s).");

        incrementAndBlock(userId, username, message, location, alerts);
    }

    private void incrementAndBlock(String userId, String username, String message, String location, List<String> alerts) {
        int total = totalFlaggedUsed.merge(userId, 1, Integer::sum);
        if (total >= TERM_LIMIT) {
            alerts.add("🚫 You have used flagged terms 3 times. You are now blocked from chatting.");
            fetchUserLocation(userId, username, message, location);
        }
    }

    private void fetchUserLocation(String userId, String username, String message, String location) {
        logger.info("Fetching location for user: " + userId);
        List<String> flaggedTerms = new ArrayList<>(userTermCounts.get(userId).keySet());
        List<String> chats = new ArrayList<>();
        chats.add(message); // Add this message as a sample
        flaggedUserDetails.putIfAbsent(userId, new FlaggedUserDetails(userId, username, location, flaggedTerms, chats));
    }

    public String resetCounts(String userId) {
        userTermCounts.remove(userId);
        totalFlaggedUsed.remove(userId);
        flaggedUserDetails.remove(userId);
        return "✅ System has been reset. You may chat again.";
    }

    public boolean isBlocked(String userId) {
        return totalFlaggedUsed.getOrDefault(userId, 0) >= TERM_LIMIT;
    }

    public List<FlaggedUserDetails> getFlaggedUsers() {
        return new ArrayList<>(flaggedUserDetails.values());
    }
}