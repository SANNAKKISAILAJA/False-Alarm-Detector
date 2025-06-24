package com.example.False.Alarm.model;

import com.example.False.Alarm.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.config.YamlProcessor;

@Entity
@Table(name = "matches")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    User receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    MatchStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conversation_id")
    Conversation conversation;

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public MatchStatus getStatus() {
        return status;
    }
}
