package com.example.messageservice.mapper;

import com.example.messageservice.dto.ParticipantDTO;
import com.example.messageservice.entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ParticipantMapper {
    @Mapping(source = "participant.participantId.conversation.conversationId",target = "conversationId")
    @Mapping(source = "participant.participantId.userId",target = "userId")
    ParticipantDTO participantToDTO(Participant participant);

    @Mapping(source = "participantDTO.conversationId",target = "participantId.conversation.conversationId")
    @Mapping(source = "participantDTO.userId",target = "participantId.userId")
    Participant participantToEntity(ParticipantDTO participantDTO);
}
