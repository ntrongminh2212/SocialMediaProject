package com.example.messageservice.controller;

import com.example.messageservice.dto.ConversationDTO;
import com.example.messageservice.dto.InviteFormDTO;
import com.example.messageservice.dto.MessageDTO;
import com.example.messageservice.dto.ParticipantDTO;
import com.example.messageservice.entity.Participant;
import com.example.messageservice.service.MessageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(path = "/message")
public class MessageController {
    Logger logger = Logger.getLogger(MessageController.class);
    @Autowired
    private MessageService messageService;
    @PostMapping("/create-conversation")
    public ResponseEntity<ConversationDTO> createConversation(ConversationDTO conversationDTO){
        conversationDTO = messageService.createConversation(conversationDTO);
        return ResponseEntity.ok(conversationDTO);
    }

    @PostMapping("/conversation-participate")
    public ResponseEntity<List<ParticipantDTO>> participateInConversation(
            @RequestBody InviteFormDTO inviteFormDTO
    ) {
        List<ParticipantDTO> participantList = messageService.participateInConversation(inviteFormDTO);
        if (participantList != null) {
            return ResponseEntity.ok(participantList);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/send")
    public ResponseEntity<MessageDTO> sendMessage(
            @RequestBody MessageDTO messageDTO
    ){
        messageDTO = messageService.sendMessage(messageDTO);
        return ResponseEntity.ok(messageDTO);
    }

    @DeleteMapping("/unsend")
    public ResponseEntity<Object> unSendMessage(
            @RequestBody MessageDTO messageDTO
    ){
        boolean rs = messageService.unSendMessage(messageDTO);
        if (rs) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().build();
    }
}









