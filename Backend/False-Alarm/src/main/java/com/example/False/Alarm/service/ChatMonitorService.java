    package com.example.False.Alarm.service;

import com.example.False.Alarm.enums.ObservationStatus;
import com.example.False.Alarm.model.User;
import com.example.False.Alarm.repository.UserRepository;
import com.example.False.Alarm.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMonitorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AIService aiService;

    public void handleToxicMessage(String userId, String message) {
        User user = userRepository.findByUserId(userId).orElse(null);
        if (user == null) {
            System.out.println("User not found for ID: " + userId);
            return;
        }

        boolean isToxic = aiService.isMessageToxic(message);

        if (!isToxic) return;

        switch (user.getObservationStatus()) {
            case NOT_OBSERVED -> {
                user.setObservationStatus(ObservationStatus.OBSERVED);
                handleWarning(user);
            }
            case OBSERVED -> handleWarning(user);
            case UNDER_ADMIN_WATCH -> handleAdminWatch(user);
            case BLOCKED, BLOCKED_REQUESTED_UNBLOCK -> System.out.println("User already blocked: " + user.getUsername());
            default -> System.out.println("Unhandled status: " + user.getObservationStatus());
        }

        userRepository.save(user);
    }

    private void handleWarning(User user) {
        int warnings = user.getWarningCount() + 1;
        user.setWarningCount(warnings);
        System.out.println("Warning " + warnings + "/5 issued to: " + user.getUsername());
        if (warnings >= 5) {
            user.setObservationStatus(ObservationStatus.UNDER_ADMIN_WATCH);
            user.setWarningCount(0);
            user.setAdminWatchCount(0);
            System.out.println("User moved to UNDER_ADMIN_WATCH: " + user.getUsername());
        }
    }

    private void handleAdminWatch(User user) {
        int adminCount = user.getAdminWatchCount() + 1;
        user.setAdminWatchCount(adminCount);

        if (adminCount >= 2) {
            user.setObservationStatus(ObservationStatus.BLOCKED);
            user.setBlockUntil(LocalDateTime.now().plusHours(24));
            user.setAdminWatchCount(0); // Resets after blocking
            System.out.println("User BLOCKED for 24 hrs: " + user.getUsername());
        } else {
            System.out.println("AdminWatch violation " + adminCount + "/2 for: " + user.getUsername());
        }
    }

    public List<User> getBlockedUsers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getObservationStatus() == ObservationStatus.BLOCKED)
                .toList();
    }

    public List<String> checkMessage(String userId, String message) {
        List<String> alerts = new java.util.ArrayList<>();
        User user = userRepository.findByUserId(userId).orElse(null);
        if (user == null) {
            alerts.add("User not found for ID: " + userId);
            return alerts;
        }
        boolean isToxic = aiService.isMessageToxic(message);
        if (!isToxic) {
            alerts.add("Message is safe.");
            return alerts;
        }
        switch (user.getObservationStatus()) {
            case NOT_OBSERVED -> {
                user.setObservationStatus(ObservationStatus.OBSERVED);
                user.setWarningCount(1);
                alerts.add("⚠ Warning 1/5: This message contains inappropriate content. You are now under observation.");
            }
            case OBSERVED -> {
                int warnings = user.getWarningCount() + 1;
                user.setWarningCount(warnings);
                alerts.add("⚠ Warning " + warnings + "/5: This message contains inappropriate content.");
                if (warnings >= 5) {
                    user.setObservationStatus(ObservationStatus.UNDER_ADMIN_WATCH);
                    user.setWarningCount(0);
                    user.setAdminWatchCount(0);
                    alerts.add("🔍 User is under admin watch. Warnings reset. Admin watch cycle started.");
                }
            }
            case UNDER_ADMIN_WATCH -> {
                int adminCount = user.getAdminWatchCount() + 1;
                user.setAdminWatchCount(adminCount);
                alerts.add("⚠ Admin Watch Offense " + adminCount + "/2: This message contains inappropriate content.");
                if (adminCount >= 2) {
                    user.setObservationStatus(ObservationStatus.BLOCKED);
                    user.setBlockUntil(java.time.LocalDateTime.now().plusHours(24));
                    user.setWarningCount(0);
                    user.setAdminWatchCount(0);
                    alerts.add("⛔ User is BLOCKED for 24 hours.");
                }
            }
            case BLOCKED, BLOCKED_REQUESTED_UNBLOCK -> alerts.add("⛔ User is already blocked.");
        }
        userRepository.save(user);
        return alerts;
    }

    public boolean isBlocked(String userId) {
        User user = userRepository.findByUserId(userId).orElse(null);
        return user != null && user.getObservationStatus() == ObservationStatus.BLOCKED;
    }

    public boolean isAdminWatch(String userId) {
        User user = userRepository.findByUserId(userId).orElse(null);
        return user != null && user.getObservationStatus() == ObservationStatus.UNDER_ADMIN_WATCH;
    }

    public int getTotalWarnings(String userId) {
        User user = userRepository.findByUserId(userId).orElse(null);
        return user != null ? user.getWarningCount() : 0;
    }

    public String resetCounts(String userId) {
        User user = userRepository.findByUserId(userId).orElse(null);
        if (user == null) return "User not found.";
        user.setWarningCount(0);
        user.setAdminWatchCount(0);
        user.setObservationStatus(ObservationStatus.OBSERVED);
        userRepository.save(user);
        return "User warning and admin watch counts reset.";
    }

    public java.util.List<User> getFlaggedUsers() {
        return userRepository.findAll().stream()
            .filter(u -> u.getObservationStatus() == ObservationStatus.UNDER_ADMIN_WATCH ||
                         u.getObservationStatus() == ObservationStatus.BLOCKED)
            .toList();
    }
}