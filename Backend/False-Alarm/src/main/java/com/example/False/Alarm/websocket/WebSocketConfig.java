package com.example.False.Alarm.websocket;

import com.example.False.Alarm.service.ChatMonitorService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatMonitorService chatMonitorService;

    public WebSocketConfig(ChatMonitorService chatMonitorService) {
        this.chatMonitorService = chatMonitorService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(
            new AlarmWebSocketHandler(chatMonitorService),
            "/ws/alarm"
        ).setAllowedOrigins("*");
    }
}
