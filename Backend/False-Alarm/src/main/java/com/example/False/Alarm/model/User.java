package com.example.False.Alarm.model;

import com.example.False.Alarm.enums.ObservationStatus;
import com.example.False.Alarm.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(length = 30)
    String username;

    @Column(length = 30, unique = true, nullable = false)
    String userId;

    @Column(unique = true, length = 50)
    String email;

    String password;

    String authorities;     //ADMIN, USER

    @Enumerated(value = EnumType.STRING)
    UserType userType; //ADMIN, USER

    @Column(length = 512)
    String profilePicUrl;

    @Column(length = 100)
    String location; 

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    List<Match> sentMatches;

    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    List<Match> receivedMatches;

    @Builder.Default
    Boolean isEnabled = false;

    @Enumerated(value = EnumType.STRING)
    ObservationStatus observationStatus;

    @CreationTimestamp
    Date createdOn;

    @UpdateTimestamp
    Date updatedOn;

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Arrays.stream(authorities.split(","))
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
