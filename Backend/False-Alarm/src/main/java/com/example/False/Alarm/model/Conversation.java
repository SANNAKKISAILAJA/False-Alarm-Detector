package com.example.False.Alarm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "conversation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Conversation {
    @Id	
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;  // The owner of the conversation

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    @JsonManagedReference
    List<ChatMessage> messages;

    public User getUser() {
        return user;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setUser(User user) {
        this.user = user;
    }


}