package com.example.False.Alarm.model;

import com.example.False.Alarm.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.config.YamlProcessor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Matches")
public class Match{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    User sender;

    @ManyToOne
    User receiver;

    @Enumerated(EnumType.STRING)
    MatchStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conversation_id")
    Conversation conversation;

}
