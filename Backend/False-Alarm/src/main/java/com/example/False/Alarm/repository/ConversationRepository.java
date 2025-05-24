package com.example.False.Alarm.repository;

import com.example.False.Alarm.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, String> {

}
