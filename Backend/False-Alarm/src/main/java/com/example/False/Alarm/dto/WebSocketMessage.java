package com.example.False.Alarm.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebSocketMessage {
    private String userId;
    private String message;
    private String username;
    private java.time.LocalDateTime time;

    public String getUserId() {
        return userId;
    }
    public String getMessage() {
        return message;
    }
    public String getUsername() {
        return username;
    }
    public java.time.LocalDateTime getTime() {
        return time;
    }
}
