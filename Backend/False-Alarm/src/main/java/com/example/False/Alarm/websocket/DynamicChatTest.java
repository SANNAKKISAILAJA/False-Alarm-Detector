package com.example.False.Alarm.websocket;

import java.util.List;
import com.example.False.Alarm.service.ChatMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.False.Alarm.websocket.InputHandler;

@SpringBootApplication
public class DynamicChatTest {

    @Autowired
    private ChatMonitorService chatMonitorService;

    public static void main(String[] args) {
        SpringApplication.run(DynamicChatTest.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            // Get initial user information
            String userId = InputHandler.getUserId();
            String username = InputHandler.getUsername();


            // Main loop for sending messages
            while (true) {
                String message = InputHandler.getMessage();
                
                // Check if user wants to exit
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                
                // Process message through chat monitor
                List<String> alerts = chatMonitorService.checkMessage(
                    userId, message
                );
                
                // Display alerts
                for (String alert : alerts) {
                    InputHandler.printAlert(alert);
                }
                
                // Show current status
                String status = "Current Status: ";
                if (chatMonitorService.isBlocked(userId)) {
                    status += "BLOCKED";
                } else if (chatMonitorService.isAdminWatch(userId)) {
                    status += "UNDER ADMIN WATCH";
                } else {
                    status += "NORMAL";
                }
                InputHandler.printStatus(status);
                
                // Show warning count if not blocked
                if (!chatMonitorService.isBlocked(userId)) {
                    int warnings = chatMonitorService.getTotalWarnings(userId);
                    InputHandler.printStatus("Warning Count: " + warnings + "/5");
                }
            }
        };
    }
}
