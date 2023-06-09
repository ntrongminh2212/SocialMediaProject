package com.example.messageservice.entity;

import com.example.messageservice.dto.ConversationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {
    @Id
    @SequenceGenerator(
            name = "conversation_sequence",
            sequenceName = "conversation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "conversation_sequence"
    )
    private Long conversationId;
    private Long userId;
    private String name;
    private ConversationType type = ConversationType.PRIVATE;
    @CreationTimestamp
    private Date createdTime;
}
