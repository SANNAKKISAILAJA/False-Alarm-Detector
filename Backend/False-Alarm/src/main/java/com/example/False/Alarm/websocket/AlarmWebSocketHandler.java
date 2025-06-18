package com.example.False.Alarm.websocket;

import com.example.False.Alarm.model.User;
import com.example.False.Alarm.service.ChatMonitorService;
import com.example.False.Alarm.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

public class AlarmWebSocketHandler extends TextWebSocketHandler {
    private final ChatMonitorService chatMonitorService;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AlarmWebSocketHandler(ChatMonitorService chatMonitorService, UserService userService) {
        this.chatMonitorService = chatMonitorService;
        this.userService = userService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String userId;
        try {
            // Option 1: Get userId from session (needs HandshakeInterceptor)
            userId = (String) session.getAttributes().get("userId");
            if (userId == null) {
                // Option 2: Try to get userId from message payload
                JsonNode json = objectMapper.readTree(message.getPayload());
                if (json.has("userId")) {
                    userId = json.get("userId").asText();
                }
            }

            if (userId == null) {
                session.sendMessage(new TextMessage("Error: User ID not found"));
                return;
            }

            User user = userService.getUserById(userId);
            if (user == null) {
                session.sendMessage(new TextMessage("Error: User not found"));
                return;
            }

            String username = user.getUsername();
            String userMessage = message.getPayload(); // or extract from JSON
            String location = user.getLocation();

            List<String> alerts = chatMonitorService.checkMessage(userId, username, userMessage, location);

            for (String alert : alerts) {
                session.sendMessage(new TextMessage(alert));
            }

            if (chatMonitorService.isBlocked(userId)) {
                String locationRequestJson = String.format(
                        "{\"type\":\"locationRequest\",\"userId\":\"%s\"}",
                        userId
                );
                session.sendMessage(new TextMessage(locationRequestJson));
            }
        } catch (Exception e) {
            session.sendMessage(new TextMessage("Error: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        System.out.println("WebSocket closed: " + session.getId());
    }
}
