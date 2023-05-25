package com.example.messageservice.repository;

import com.example.messageservice.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface MessageRepository extends JpaRepository<Message,Long> {
    Optional<Message> findByMessageIdAndUserId(Long messageId, Long userId);
}
