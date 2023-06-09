package com.example.friendservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDTO {
    private Long conversationId;
    private Long userId;
    private String name;
    private Date createdTime;
}
