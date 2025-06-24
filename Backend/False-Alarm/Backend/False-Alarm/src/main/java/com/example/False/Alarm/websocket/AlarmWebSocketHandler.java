package com.example.False.Alarm.websocket;

import java.util.Map;
import java.util.List;
import com.example.False.Alarm.service.ChatMonitorService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(AlarmWebSocketHandler.class);
    private final ChatMonitorService chatMonitorService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AlarmWebSocketHandler(ChatMonitorService chatMonitorService) {
        this.chatMonitorService = chatMonitorService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("WebSocket connected: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            // Parse incoming message
            ChatMessageDTO chatMessage = objectMapper.readValue(message.getPayload(), ChatMessageDTO.class);
            String userId = chatMessage.getUserId();
            String messageContent = chatMessage.getMessage();
            String username = chatMessage.getUsername();
            String location = chatMessage.getLocation();

            // Check message with ChatMonitorService
            List<String> alerts = chatMonitorService.checkMessage(userId, username, messageContent, location);

            // Send alerts to client
            for (String alert : alerts) {
                session.sendMessage(new TextMessage(alert));
            }

            // If user is blocked, request location
            if (chatMonitorService.isBlocked(userId)) {
                String locationRequestJson = objectMapper.writeValueAsString(
                    Map.of("type", "locationRequest", "userId", userId)
                );
                session.sendMessage(new TextMessage(locationRequestJson));
            }
        } catch (Exception e) {
            logger.error("Error processing WebSocket message: {}", e.getMessage(), e);
            session.sendMessage(new TextMessage("Error processing message. Please try again."));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        logger.info("WebSocket closed: {} with status: {}", session.getId(), status);
    }

    private static class ChatMessageDTO {
        private String userId;
        private String message;
        private String username;
        private String location;

        // Getters and setters
        public String getUserId() { return userId; }
        public String getMessage() { return message; }
        public String getUsername() { return username; }
        public String getLocation() { return location; }
    }
}
