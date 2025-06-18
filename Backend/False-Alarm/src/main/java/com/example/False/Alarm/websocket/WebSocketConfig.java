package com.example.False.Alarm.websocket;

import com.example.False.Alarm.service.ChatMonitorService;
import com.example.False.Alarm.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatMonitorService chatMonitorService;
    private final UserService userService;

    public WebSocketConfig(ChatMonitorService chatMonitorService, UserService userService) {
        this.chatMonitorService = chatMonitorService;
        this.userService = userService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(
            new AlarmWebSocketHandler(chatMonitorService, userService),
            "/ws/alarm"
        ).setAllowedOrigins("*");
    }
}
