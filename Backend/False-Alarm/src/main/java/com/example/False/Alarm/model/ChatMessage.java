package com.example.False.Alarm.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage{
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        String id;

        String otherEndUserId;

        String messageText;

        LocalDateTime time;

        @ManyToOne
        @JoinColumn(name = "conversation_id")
        Conversation conversationId;
}
