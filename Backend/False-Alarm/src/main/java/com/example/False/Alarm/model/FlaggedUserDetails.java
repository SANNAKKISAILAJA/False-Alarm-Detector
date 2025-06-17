package com.example.False.Alarm.model;

import java.util.List;

public class FlaggedUserDetails {
    private String userId;
    private String username;
    private String location;
    private List<String> flaggedTerms;
    private List<String> chats;

    public FlaggedUserDetails() {}
    public FlaggedUserDetails(String userId, String username, String location, List<String> flaggedTerms, List<String> chats) {
        this.userId = userId;
        this.username = username;
        this.location = location;
        this.flaggedTerms = flaggedTerms;
        this.chats = chats;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public List<String> getFlaggedTerms() { return flaggedTerms; }
    public void setFlaggedTerms(List<String> flaggedTerms) { this.flaggedTerms = flaggedTerms; }
    public List<String> getChats() { return chats; }
    public void setChats(List<String> chats) { this.chats = chats; }
}
