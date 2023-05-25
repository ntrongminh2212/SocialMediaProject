package com.example.messageservice.repository;

import com.example.messageservice.entity.Participant;
import com.example.messageservice.id.ParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, ParticipantId> {
    Optional<Participant> findByUserIdAndConversationId(Long userId, Long conversationId);
}
