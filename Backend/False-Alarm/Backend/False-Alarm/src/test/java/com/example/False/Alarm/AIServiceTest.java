package com.example.False.Alarm;

import com.example.False.Alarm.service.AIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AIServiceTest {

    @Autowired
    private AIService aiService;

    @Test
    public void testToxicMessageDetection() {
        // Test a known toxic message
        boolean isToxic = aiService.isMessageToxic("You are a complete idiot");
        assertTrue(isToxic, "The toxic message should be detected as toxic");

        // Test a non-toxic message
        boolean isNotToxic = aiService.isMessageToxic("Hello, how are you?");
        assertFalse(isNotToxic, "The non-toxic message should not be detected as toxic");
    }
}
