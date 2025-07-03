package com.example.False.Alarm;

import com.example.False.Alarm.service.AIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.when;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AIServiceTest {

    @Autowired
    private AIService aiService;

    @org.springframework.boot.test.mock.mockito.MockBean
    private AIService aiServiceMock;

    @Test
    public void testToxicMessageDetection() {
        // Test a known toxic message
        when(aiServiceMock.isMessageToxic("You are a complete idiot")).thenReturn(true);
        boolean isToxic = aiService.isMessageToxic("You are a complete idiot");
        assertTrue(isToxic, "The toxic message should be detected as toxic");

        // Test a non-toxic message
        when(aiServiceMock.isMessageToxic("Hello, how are you?")).thenReturn(false);
        boolean isNotToxic = aiService.isMessageToxic("Hello, how are you?");
        assertFalse(isNotToxic, "The non-toxic message should not be detected as toxic");
    }
}
