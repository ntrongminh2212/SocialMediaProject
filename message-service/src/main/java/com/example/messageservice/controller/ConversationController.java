package com.example.messageservice.controller;

import com.example.messageservice.dto.*;
import com.example.messageservice.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

@Controller
//@RequestMapping(path = "/message")
public class ConversationController {
    Logger logger = Logger.getLogger(ConversationController.class);
    @Autowired
    private MessageService messageService;
    @PostMapping("/message/create-conversation")
    public ResponseEntity<Object> createConversation(@RequestBody ConversationDTO conversationDTO){
        if (conversationDTO.getUserId()!=null) {
            conversationDTO = messageService.createConversation(conversationDTO);
            return ResponseEntity.ok(conversationDTO);
        }
        ResponseDTO.BADREQUEST.put("message","Not an user");
        return ResponseEntity.badRequest().body(ResponseDTO.BADREQUEST);
    }

    @PostMapping("/message/conversation-participate")
    public ResponseEntity<List<ParticipantDTO>> participateInConversation(
            @RequestBody InviteFormDTO inviteFormDTO
    ) {
        List<ParticipantDTO> participantList = messageService.participateInConversation(inviteFormDTO);
        if (participantList != null) {
            return ResponseEntity.ok(participantList);
        }
        return ResponseEntity.badRequest().build();
    }

    @MessageMapping("/send/{conversationId}")
    @SendTo("/conversation/{conversationId}")
    public ResponseEntity<Object> sendMessage(
            @DestinationVariable Long conversationId,
            @Payload Message<MessageDTO> message
    ) {
        MessageDTO messageDTO = message.getPayload();
        LinkedMultiValueMap<String, Object> nativeHeaders = (LinkedMultiValueMap<String, Object>) message.getHeaders().get("nativeHeaders");
        Long userId = Long.parseLong(nativeHeaders.get("userId").get(0).toString());
        if (userId!=null) {
            messageDTO.setUserId(userId);
            messageDTO =messageService.sendMessage(messageDTO);
            return ResponseEntity.ok(messageDTO);
        }
        return ResponseEntity.badRequest().body(ResponseDTO.BADREQUEST);
    }

    @DeleteMapping("/message/unsend")
    public ResponseEntity<Object> unSendMessage(
            @RequestBody MessageDTO messageDTO
    ){
        boolean rs = messageService.unSendMessage(messageDTO);
        if (rs) {
            return ResponseEntity.ok(true);
        }
        ResponseDTO.BADREQUEST
                .put("message", "User's not own this message");
        return ResponseEntity.badRequest().body(
                ResponseDTO.BADREQUEST
        );
    }
g
    @GetMapping("/message")
    public ResponseEntity<Object> getConversationMessages(
        @RequestParam("conversation_id") Long conversationId,
        final HttpServletRequest request
    ){
        if (request.getHeader("userId") !=null) {
            Long userId = Long.parseLong(request.getHeader("userId"));
            if (messageService.isParticipant(userId,conversationId)) {
                List<MessageDTO> messageDTOList = messageService.getMsgsByConversationId(conversationId);
                return ResponseEntity.ok(messageDTOList);
            }
            ResponseDTO.BADREQUEST.put("message","Not a participant");
            return ResponseEntity.badRequest().body(ResponseDTO.BADREQUEST);
        }
        return ResponseEntity.badRequest().body(ResponseDTO.BADREQUEST);
    }
}









