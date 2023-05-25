package com.example.messageservice.mapper;

import com.example.messageservice.dto.MessageDTO;
import com.example.messageservice.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {

    @Mapping(source = "message.conversation.conversationId",target = "conversationId")
    MessageDTO messageToDTO(Message message);
    @Mapping(source = "messageDTO.conversationId",target = "conversation.conversationId")
    Message messageToEntity(MessageDTO messageDTO);
}
