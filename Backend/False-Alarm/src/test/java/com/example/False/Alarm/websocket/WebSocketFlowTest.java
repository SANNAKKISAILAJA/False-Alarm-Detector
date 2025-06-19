package com.example.False.Alarm.websocket;

import java.util.List;
import com.example.False.Alarm.service.ChatMonitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class WebSocketFlowTest {

    @Autowired
    private ChatMonitorService chatMonitorService;

    private String testUserId = "test-user";
    private String testUsername = "Test User";
    private String testLocation = "123.456,789.012";

    @BeforeEach
    public void setUp() {
        // Reset user state before each test
        chatMonitorService.resetCounts(testUserId);
    }

    @Test
    public void testNonToxicMessageFlow() {
        // Send a non-toxic message
        List<String> alerts = chatMonitorService.checkMessage(
            testUserId, testUsername, "Hello, how are you?", testLocation
        );

        // Verify message is accepted
        assertEquals(1, alerts.size());
        assertEquals("✔ Message accepted.", alerts.get(0));
    }

    @Test
    public void testToxicMessageFlow() {
        // Send toxic messages and verify warnings
        for (int i = 1; i <= 5; i++) {
            List<String> alerts = chatMonitorService.checkMessage(
                testUserId, testUsername, "You are a complete idiot", testLocation
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
            chatMonitorService.checkMessage(
                testUserId, testUsername, "You are a complete idiot", testLocation
            );
        }

        // Send another toxic message
        List<String> alerts = chatMonitorService.checkMessage(
            testUserId, testUsername, "You are a complete idiot", testLocation
        );

        // Verify blocking message
        assertEquals(1, alerts.size());
        assertTrue(alerts.get(0).startsWith("❌ You have been temporarily blocked for 24 hours"));

        // Verify user is blocked
        assertTrue(chatMonitorService.isBlocked(testUserId));
    }
}
