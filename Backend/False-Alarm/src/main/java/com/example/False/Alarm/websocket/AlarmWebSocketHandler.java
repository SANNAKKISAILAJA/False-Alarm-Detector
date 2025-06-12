package com.example.False.Alarm.websocket;

import java.util.List;
import com.example.False.Alarm.service.ChatMonitorService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class AlarmWebSocketHandler extends TextWebSocketHandler {

    private final ChatMonitorService chatMonitorService;

    public AlarmWebSocketHandler(ChatMonitorService chatMonitorService) {
        this.chatMonitorService = chatMonitorService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String userId = session.getId(); // Or extract userId from message/session attributes
        String userMessage = message.getPayload();

        // Check message with ChatMonitorService
        List<String> alerts = chatMonitorService.checkMessage(userId, userMessage);

        // Send alerts to client
        for (String alert : alerts) {
            session.sendMessage(new TextMessage(alert));
        }

        // If user is blocked, request location
        if (chatMonitorService.isBlocked(userId)) {
            String locationRequestJson = String.format(
                "{\"type\":\"locationRequest\",\"userId\":\"%s\"}",
                userId
            );
            session.sendMessage(new TextMessage(locationRequestJson));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        System.out.println("WebSocket closed: " + session.getId());
    }
}
