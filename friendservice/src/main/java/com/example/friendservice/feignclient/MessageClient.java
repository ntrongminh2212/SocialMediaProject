package com.example.friendservice.feignclient;

import com.example.friendservice.dto.ConversationDTO;
import com.example.friendservice.dto.InviteFormDTO;
import com.example.friendservice.dto.ParticipantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "MESSAGE-SERVICE",path = "/message/")
public interface MessageClient {
    @PostMapping("/create-conversation")
    ResponseEntity<ConversationDTO> createConversation(ConversationDTO conversationDTO);

    @PostMapping("/conversation-participate")
    ResponseEntity<List<ParticipantDTO>> participateInConversation(
            @RequestBody InviteFormDTO inviteFormDTO
    );
}
