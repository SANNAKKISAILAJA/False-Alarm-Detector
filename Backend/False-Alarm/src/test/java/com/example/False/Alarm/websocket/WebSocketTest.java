package com.example.False.Alarm.websocket;

import java.util.List;

import com.example.False.Alarm.service.ChatMonitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WebSocketTest {

    @Mock
    private ChatMonitorService chatMonitorService;

    @Mock
    private WebSocketSession session;

    private AlarmWebSocketHandler webSocketHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        webSocketHandler = new AlarmWebSocketHandler(chatMonitorService);
    }

    @Test
    public void testNonToxicMessage() throws Exception {
        // Create test message
        String userId = "test-user";
        String username = "Test User";
        String message = "Hello, how are you?";
        
        // Mock ChatMonitorService response
        when(chatMonitorService.checkMessage(userId, message))
            .thenReturn(List.of("✔ Message accepted."));
        
        // Create WebSocket message
        String jsonMessage = String.format("{\"userId\":\"%s\",\"message\":\"%s\",\"username\":\"%s\"}",
            userId, message, username);
        TextMessage textMessage = new TextMessage(jsonMessage);
        
        // Process message
        webSocketHandler.handleTextMessage(session, textMessage);
        
        // Verify message was accepted
        verify(session).sendMessage(new TextMessage("✔ Message accepted."));
        verify(chatMonitorService).checkMessage(userId, message);
        verify(chatMonitorService).isBlocked(userId);
    }

    @Test
    public void testToxicMessage() throws Exception {
        // Create test message
        String userId = "test-user";
        String username = "Test User";
        String message = "You are a complete idiot";
        
        // Mock ChatMonitorService response
        when(chatMonitorService.checkMessage(userId, message))
            .thenReturn(List.of("⚠ Warning 1/5: This message contains inappropriate content."));
        
        // Create WebSocket message
        String jsonMessage = String.format("{\"userId\":\"%s\",\"message\":\"%s\",\"username\":\"%s\"}",
            userId, message, username);
        TextMessage textMessage = new TextMessage(jsonMessage);
        
        // Process message
        webSocketHandler.handleTextMessage(session, textMessage);
        
        // Verify warning was sent
        verify(session).sendMessage(new TextMessage("⚠ Warning 1/5: This message contains inappropriate content."));
        verify(chatMonitorService).checkMessage(userId, message);
        verify(chatMonitorService).isBlocked(userId);
    }

    @Test
    public void testBlockedUser() throws Exception {
        // Create test message
        String userId = "test-user";
        String username = "Test User";
        String message = "You are a complete idiot";
        
        // Mock ChatMonitorService response
        when(chatMonitorService.checkMessage(userId, message))
            .thenReturn(List.of("❌ You have been temporarily blocked for 24 hours due to continued inappropriate behavior."));
        when(chatMonitorService.isBlocked(userId)).thenReturn(true);
        
        // Create WebSocket message
        String jsonMessage = String.format("{\"userId\":\"%s\",\"message\":\"%s\",\"username\":\"%s\"}",
            userId, message, username);
        TextMessage textMessage = new TextMessage(jsonMessage);
        
        // Process message
        webSocketHandler.handleTextMessage(session, textMessage);
        
        // Verify blocked message was sent
        verify(session).sendMessage(new TextMessage("❌ You are currently blocked. Please contact admin for assistance."));
        verify(chatMonitorService).checkMessage(userId, message);
        verify(chatMonitorService).isBlocked(userId);
    }
}
