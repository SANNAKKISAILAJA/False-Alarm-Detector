package com.example.False.Alarm.model;

import com.example.False.Alarm.enums.ObservationStatus;

public class FlaggedUserDetails {
    private String userId;
    private String username;
    private ObservationStatus observationStatus;
    private int warningCount;
    private String flaggedMessages;
    private String flaggedTerms;

    public FlaggedUserDetails() {}
    
    public FlaggedUserDetails(String userId, String username, ObservationStatus status, int warnings, String flaggedMessages, String flaggedTerms) {
        this.userId = userId;
        this.username = username;
        this.observationStatus = status;
        this.warningCount = warnings;
        this.flaggedMessages = flaggedMessages;
        this.flaggedTerms = flaggedTerms;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public ObservationStatus getObservationStatus() { return observationStatus; }
    public void setObservationStatus(ObservationStatus status) { this.observationStatus = status; }
    public int getWarningCount() { return warningCount; }
    public void setWarningCount(int warnings) { this.warningCount = warnings; }
    public String getFlaggedMessages() { return flaggedMessages; }
    public void setFlaggedMessages(String messages) { this.flaggedMessages = messages; }
    public String getFlaggedTerms() { return flaggedTerms; }
    public void setFlaggedTerms(String terms) { this.flaggedTerms = terms; }
}
