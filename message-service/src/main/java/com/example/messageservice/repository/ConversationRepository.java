package com.example.messageservice.repository;

import com.example.messageservice.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation,Long> {
    Optional<Conversation> findByUserIdAndConversationId(Long userId, Long conversationId);
}
