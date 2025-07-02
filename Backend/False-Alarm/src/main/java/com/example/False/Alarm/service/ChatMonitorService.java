package com.example.False.Alarm.service;

import com.example.False.Alarm.enums.FlaggedTerms;
import com.example.False.Alarm.model.FlaggedUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ChatMonitorService {
    private static final Logger logger = LoggerFactory.getLogger(ChatMonitorService.class);
    private static final int WARNING_LIMIT = 5;
    private static final int BLOCK_DURATION_HOURS = 24; // Block duration in hours

    private final Map<String, Map<String, Integer>> userTermCounts = new ConcurrentHashMap<>();
    private final Map<String, Integer> totalWarnings = new ConcurrentHashMap<>();
    private final Map<String, Boolean> underAdminWatch = new ConcurrentHashMap<>();
    private final Map<String, Long> blockUntil = new ConcurrentHashMap<>(); // Timestamp when block ends
    private final Map<String, FlaggedUserDetails> flaggedUserDetails = new ConcurrentHashMap<>();
    
    private final AIService aiService;
    private final ScheduledExecutorService scheduler;

    @Autowired
    public ChatMonitorService(AIService aiService) {
        this.aiService = aiService;
        this.scheduler = new ScheduledThreadPoolExecutor(1);
        
        // Start a background task to check and unblock users
        scheduler.scheduleAtFixedRate(this::checkAndUnblockUsers, 0, 1, TimeUnit.HOURS);
    }

    private void checkAndUnblockUsers() {
        long currentTime = System.currentTimeMillis();
        blockUntil.forEach((userId, blockTime) -> {
            if (currentTime > blockTime) {
                blockUntil.remove(userId);
                logger.info("User {} has been automatically unblocked", userId);
            }
        });
    }

    public List<String> checkMessage(String userId, String username, String message, String location) {
        List<String> alerts = new ArrayList<>();
        String messageLower = message.toLowerCase();

        userTermCounts.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        totalWarnings.putIfAbsent(userId, 0);
        underAdminWatch.putIfAbsent(userId, false);

        // Check if user is currently blocked
        if (isBlocked(userId)) {
            long remainingTime = (blockUntil.get(userId) - System.currentTimeMillis()) / (1000 * 60);
            alerts.add(String.format("❌ You are temporarily blocked. Please wait %d minutes before trying again.", remainingTime));
            return alerts;
        }

        // AI check
        try {
            if (aiService.isMessageToxic(message)) {
                handleToxicMessage(userId, username, message, location, alerts);
                return alerts;
            }
        } catch (Exception e) {
            logger.warn("AI service check failed, falling back to basic checks", e);
        }

        // Basic term check
        for (String term : FlaggedTerms.getAllTerms()) {
            if (messageLower.contains(term)) {
                handleToxicMessage(userId, username, message, location, alerts);
                break;
            }
        }

        return alerts.isEmpty() ? List.of("✔ Message accepted.") : alerts;
    }

    private void handleToxicMessage(String userId, String username, String message, String location, List<String> alerts) {
        Map<String, Integer> termCounts = userTermCounts.get(userId);
        int currentWarnings = totalWarnings.get(userId) + 1; // Increment once
        totalWarnings.put(userId, currentWarnings);          // Update map

        if (currentWarnings < WARNING_LIMIT) {
            alerts.add(String.format("⚠ Warning %d/%d: This message contains inappropriate content.",
            currentWarnings, WARNING_LIMIT));
    } else if (currentWarnings == WARNING_LIMIT) {
        underAdminWatch.put(userId, true);
        fetchUserLocation(userId, username, message, location);
        alerts.add("⚠ You have reached 5 warnings. You are now under admin watch.");
    } else if (currentWarnings > WARNING_LIMIT && underAdminWatch.getOrDefault(userId, false)) {
        long blockTime = System.currentTimeMillis() + (BLOCK_DURATION_HOURS * 60 * 60 * 1000);
        blockUntil.put(userId, blockTime);
        alerts.add("❌ You have been temporarily blocked for 24 hours due to continued inappropriate behavior.");
    }
}


    public String resetCounts(String userId) {
        userTermCounts.remove(userId);
        totalWarnings.remove(userId);
        underAdminWatch.remove(userId);
        blockUntil.remove(userId);
        return "User state reset";
    }

    public boolean isBlocked(String userId) {
        return blockUntil.containsKey(userId);
    }

    public List<FlaggedUserDetails> getFlaggedUsers() {
        return new ArrayList<>(flaggedUserDetails.values());
    }

    public boolean isAdminWatch(String userId) {
        return underAdminWatch.getOrDefault(userId, false);
    }

    public int getTotalWarnings(String userId) {
        Integer warnings = totalWarnings.get(userId);
        return (warnings != null) ? warnings : 0;
    }

    public Map<String, Long> getBlockedUsers() {
        return new HashMap<>(blockUntil);
    }

    private void fetchUserLocation(String userId, String username, String message, String location) {
        logger.info("Fetching location for user: " + userId);
        List<String> flaggedTerms = new ArrayList<>(userTermCounts.get(userId).keySet());
        List<String> chats = new ArrayList<>();
        chats.add(message); // Add this message as a sample
        flaggedUserDetails.putIfAbsent(userId, new FlaggedUserDetails());
    }
}