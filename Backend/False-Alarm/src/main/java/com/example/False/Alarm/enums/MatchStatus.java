package com.example.False.Alarm.enums;

public enum MatchStatus {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    private final String value;

    MatchStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MatchStatus fromValue(String value) {
        for (MatchStatus status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid match status: " + value);
    }
}
