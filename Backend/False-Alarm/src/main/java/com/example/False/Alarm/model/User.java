package com.example.False.Alarm.model;

import com.example.False.Alarm.enums.ObservationStatus;
import com.example.False.Alarm.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    public Integer getId() {
        return id;
    }

    @Column(name = "createdOn")
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(name = "userId", length = 30, nullable = false, unique = true)
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "userType", length = 25, nullable = false)
    private UserType userType;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Column(name = "username", length = 30)
    String username;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "observationStatus", nullable = false)
    @Builder.Default
    private ObservationStatus observationStatus = ObservationStatus.NOT_OBSERVED;

    @Column(name = "authorities", length = 25)
    private String authorities;

    @Column(name = "warningCount", nullable = false)
    @Builder.Default
    Integer warningCount = 0;

    @Column(name = "blockUntil")
    private LocalDateTime blockUntil;

    @Column(name = "unblockRequested", columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private boolean unblockRequested = false;

    public boolean isUnblockRequested() {
        return unblockRequested;
    }

    public void setUnblockRequested(boolean unblockRequested) {
        this.unblockRequested = unblockRequested;
    }

    @Column(name = "flaggedMessages", length = 1000)
    @Builder.Default
    private String flaggedMessages = "";

    @Column(name = "flaggedTerms", length = 1000)
    @Builder.Default
    private String flaggedTerms = "";

    @Column(name = "isEnabled", columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default
    private boolean isEnabled = true;

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
    private List<Match> sentMatches = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
    private List<Match> receivedMatches = new ArrayList<>();

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities != null && !authorities.isEmpty()) {
            return List.of(new SimpleGrantedAuthority(authorities));
        }
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public ObservationStatus getObservationStatus() {
        return observationStatus;
    }

    public void setObservationStatus(ObservationStatus status) {
        this.observationStatus = status;
    }

    public static boolean isObserved(ObservationStatus status) {
        return status == ObservationStatus.OBSERVED ||
               status == ObservationStatus.UNDER_ADMIN_WATCH ||
               status == ObservationStatus.BLOCKED ||
               status == ObservationStatus.BLOCKED_REQUESTED_UNBLOCK;
    }

    public LocalDateTime getBlockUntil() {
        return blockUntil;
    }

    public void setBlockUntil(LocalDateTime blockUntil) {
        this.blockUntil = blockUntil;
    }

    public Integer getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(Integer warningCount) {
        this.warningCount = warningCount;
    }

    public String getFlaggedMessages() {
        return flaggedMessages;
    }

    public void setFlaggedMessages(String flaggedMessages) {
        this.flaggedMessages = flaggedMessages;
    }

    public String getFlaggedTerms() {
        return flaggedTerms;
    }

    public void setFlaggedTerms(String flaggedTerms) {
        this.flaggedTerms = flaggedTerms;
    }

    @Column(name = "adminWatchCount")
    @Builder.Default
    private Integer adminWatchCount = 0;

    public Integer getAdminWatchCount() {
        return adminWatchCount;
    }

    public void setAdminWatchCount(Integer adminWatchCount) {
        this.adminWatchCount = adminWatchCount;
    }
}