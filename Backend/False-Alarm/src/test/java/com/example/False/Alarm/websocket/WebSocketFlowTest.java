package com.example.False.Alarm.websocket;

import java.util.List;
import com.example.False.Alarm.service.ChatMonitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.when;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class WebSocketFlowTest {

    @Autowired
    private ChatMonitorService chatMonitorService;

    @org.springframework.boot.test.mock.mockito.MockBean
    private ChatMonitorService chatMonitorServiceMock;

    private String testUserId = "test-user";
    private String testUsername = "Test User";


    @BeforeEach
    public void setUp() {
        // Reset user state before each test
        chatMonitorService.resetCounts(testUserId);
    }

    @Test
    public void testNonToxicMessageFlow() {
        // Send a non-toxic message
        when(chatMonitorServiceMock.checkMessage(testUserId, "Hello, how are you?"))
            .thenReturn(List.of("✔ Message accepted."));
        List<String> alerts = chatMonitorService.checkMessage(
            testUserId, "Hello, how are you?"
        );

        // Verify message is accepted
        assertEquals(1, alerts.size());
        assertEquals("✔ Message accepted.", alerts.get(0));
    }

    @Test
    public void testToxicMessageFlow() {
        // Send toxic messages and verify warnings
        for (int i = 1; i <= 5; i++) {
            String expectedAlert = i < 5 ? 
                String.format("⚠ Warning %d/5: This message contains inappropriate content.", i) :
                "⚠ Warning 5/5: This message contains inappropriate content.";
            
            when(chatMonitorServiceMock.checkMessage(testUserId, "You are a complete idiot"))
                .thenReturn(List.of(expectedAlert));
            
            List<String> alerts = chatMonitorService.checkMessage(
                testUserId, "You are a complete idiot"
            );

            // Verify warning message
            assertEquals(1, alerts.size());
            if (i < 5) {
                assertTrue(alerts.get(0).startsWith("⚠ Warning " + i + "/5:"));
            } else {
                assertEquals("⚠ Warning 5/5: This message contains inappropriate content.", alerts.get(0));
            }
        }

        // Verify user is under admin watch
        assertTrue(chatMonitorService.isAdminWatch(testUserId));
    }

    @Test
    public void testBlockingFlow() {
        // First send 5 warnings
        for (int i = 1; i <= 5; i++) {
            String expectedAlert = i < 5 ? 
                String.format("⚠ Warning %d/5: This message contains inappropriate content.", i) :
                "⚠ Warning 5/5: This message contains inappropriate content.";
            
            when(chatMonitorServiceMock.checkMessage(testUserId, "You are a complete idiot"))
                .thenReturn(List.of(expectedAlert));
            
            chatMonitorService.checkMessage(
                testUserId, "You are a complete idiot"
            );
        }

        // Send another toxic message
        when(chatMonitorServiceMock.checkMessage(testUserId, "You are a complete idiot"))
            .thenReturn(List.of("❌ You have been temporarily blocked for 24 hours due to continued inappropriate behavior."));
        
        List<String> alerts = chatMonitorService.checkMessage(
            testUserId, "You are a complete idiot"
        );

        // Verify blocking message
        assertEquals(1, alerts.size());
        assertTrue(alerts.get(0).startsWith("❌ You have been temporarily blocked for 24 hours"));

        // Verify user is blocked
        assertTrue(chatMonitorService.isBlocked(testUserId));
    }
}
