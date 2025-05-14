package com.example.False.Alarm.model;

import com.example.False.Alarm.enums.ObservationStatus;
import com.example.False.Alarm.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(length = 30)
    String username;

    @Column(length = 30, unique = true, nullable = false)
    String userId;

    @Column(unique = true,length = 50)
    String email;

//    String password;

//    String authorities;     //ADMIN, USER

    @Enumerated(value = EnumType.STRING)
    UserType userType; //ADMIN, USER

    @Column(length = 512)
    String profilePicUrl;

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

}
