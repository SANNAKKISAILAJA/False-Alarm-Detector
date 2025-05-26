package com.example.False.Alarm.websocket;

public class Message {
    private String type;
    private String content;

    // Constructors
    public Message() {}
    public Message(String type, String content) {
        this.type = type;
        this.content = content;
    }

    // Getters & Setters
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
