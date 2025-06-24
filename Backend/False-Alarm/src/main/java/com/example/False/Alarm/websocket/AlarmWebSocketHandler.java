package com.example.False.Alarm.websocket;

import java.util.List;
import java.time.LocalDateTime;
import com.example.False.Alarm.service.ChatMonitorService;
import com.example.False.Alarm.dto.WebSocketMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;

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
            WebSocketMessage wsMessage = objectMapper.readValue(message.getPayload(), WebSocketMessage.class);
            String userId = wsMessage.getUserId();
            String messageContent = wsMessage.getMessage();
            String username = wsMessage.getUsername();
            LocalDateTime time = wsMessage.getTime();

            if (chatMonitorService.isBlocked(userId)) {
                session.sendMessage(new TextMessage("❌ You are currently blocked. Please contact admin for assistance."));
                return;
            }

            List<String> alerts = chatMonitorService.checkMessage(userId, messageContent);

            for (String alert : alerts) {
                session.sendMessage(new TextMessage(alert));
            }

            logger.info("Message received from {}: {}", username, messageContent);
        } catch (Exception e) {
            logger.error("Error processing WebSocket message: {}", e.getMessage(), e);
            session.sendMessage(new TextMessage("Error processing message. Please try again."));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("WebSocket closed: {} with status: {}", session.getId(), status);
    }
}
