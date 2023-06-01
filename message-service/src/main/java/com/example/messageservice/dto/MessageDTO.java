package com.example.messageservice.dto;

import com.example.messageservice.entity.Conversation;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String nickname;
    private Long messageId;
    private Long conversationId;
    private String content;
    private Date sendTime;
    private Long userId;
}
