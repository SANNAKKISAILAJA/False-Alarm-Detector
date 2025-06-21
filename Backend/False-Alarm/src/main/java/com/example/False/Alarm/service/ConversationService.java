package com.example.False.Alarm.service;

import com.example.False.Alarm.model.ChatMessage;
import com.example.False.Alarm.model.Conversation;
import com.example.False.Alarm.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConversationService {

    final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Conversation addMessageToConversation(Conversation conversation, ChatMessage chatMessage) {
        ChatMessage messageWithTime = ChatMessage.builder()
                .messageText(chatMessage.getMessageText())
                .otherEndUserId(chatMessage.getOtherEndUserId())
                .time(LocalDateTime.now())
                .conversation(conversation)
                .build();

        conversation.getMessages().add(messageWithTime);
        return conversationRepository.save(conversation);
    }

}
