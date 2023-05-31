package com.example.messageservice.service;

import com.example.messageservice.dto.ConversationDTO;
import com.example.messageservice.dto.InviteFormDTO;
import com.example.messageservice.dto.MessageDTO;
import com.example.messageservice.dto.ParticipantDTO;

import java.util.List;

public interface MessageService {
    ConversationDTO createConversation(ConversationDTO conversationDTO);

    List<ParticipantDTO> participateInConversation(InviteFormDTO participantDTO);

    MessageDTO sendMessage(MessageDTO messageDTO);

    boolean unSendMessage(MessageDTO messageDTO);

    boolean isParticipant(Long userId, Long conversationId);

    List<MessageDTO> getMsgsByConversationId(Long conversationId);
}
