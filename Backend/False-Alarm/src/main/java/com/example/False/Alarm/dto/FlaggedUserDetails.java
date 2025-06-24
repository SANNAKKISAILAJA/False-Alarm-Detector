package com.example.False.Alarm.dto;

import com.example.False.Alarm.enums.ObservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlaggedUserDetails {
    private String userId;
    private String username;
    private ObservationStatus observationStatus;
    private int warningCount;
    private String flaggedMessages;
    private String flaggedTerms;
}
