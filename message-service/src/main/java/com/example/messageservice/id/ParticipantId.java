package com.example.messageservice.id;

import com.example.messageservice.entity.Conversation;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantId {
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
    private Long userId;
}
