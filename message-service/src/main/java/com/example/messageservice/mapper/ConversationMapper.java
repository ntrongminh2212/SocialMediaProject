package com.example.messageservice.mapper;

import com.example.messageservice.dto.ConversationDTO;
import com.example.messageservice.entity.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConversationMapper {
    ConversationDTO conversationToDTO(Conversation conversation);
    Conversation conversationToEntity(ConversationDTO conversationDTO);
}
