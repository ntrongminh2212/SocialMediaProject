package com.example.messageservice.service;

import com.example.messageservice.dto.*;
import com.example.messageservice.entity.Conversation;
import com.example.messageservice.entity.Message;
import com.example.messageservice.entity.Participant;
import com.example.messageservice.feignclient.UserClient;
import com.example.messageservice.mapper.ConversationMapper;
import com.example.messageservice.mapper.MessageMapper;
import com.example.messageservice.mapper.ParticipantMapper;
import com.example.messageservice.repository.ConversationRepository;
import com.example.messageservice.repository.MessageRepository;
import com.example.messageservice.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceimpl implements MessageService {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ConversationMapper conversationMapper;
    @Autowired
    private ParticipantMapper participantMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserClient userClient;

    @Override
    public ConversationDTO createConversation(ConversationDTO conversationDTO) {
        Conversation conversation = conversationMapper.conversationToEntity(conversationDTO);
        return conversationMapper.conversationToDTO(
                conversationRepository.save(conversation)
        );
    }

    @Override
    public List<ParticipantDTO> participateInConversation(InviteFormDTO inviteFormDTO) {
        List<ParticipantDTO> participantList = new ArrayList<>();
        Long user_id = inviteFormDTO.getUser_id();
        Long conversationId = inviteFormDTO.getLstParticipantDTO().get(0).getConversationId();

        Optional<Participant> invitor = participantRepository.findByUserIdAndConversationId(user_id,conversationId);
        Optional<Conversation> conversationOwner = conversationRepository.findByUserIdAndConversationId(user_id,conversationId);
        if(invitor.isPresent()|| conversationOwner.isPresent()){
            for (ParticipantDTO participantDTO:
                 inviteFormDTO.getLstParticipantDTO()) {
                 Participant participant = participantMapper.participantToEntity(participantDTO);
                 participantList.add(
                         participantMapper.participantToDTO(participantRepository.save(participant))
                 );
            }
        }
        return participantList;
    }

    @Override
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        Message message = messageMapper.messageToEntity(messageDTO);
        messageDTO = messageMapper.messageToDTO(
                messageRepository.save(message)
        );
        messageDTO.setNickname(
                participantRepository.findByUserIdAndConversationId(
                        messageDTO.getUserId(),
                        messageDTO.getConversationId()
                ).get().getNickName()
        );
        return messageDTO;
    }

    @Override
    public boolean unSendMessage(MessageDTO messageDTO) {
        Optional<Message> messageOptional = messageRepository.findByMessageIdAndUserId(
                messageDTO.getMessageId(),
                messageDTO.getUserId()
        );
        if (messageOptional.isPresent()){
            messageRepository.delete(messageOptional.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean isParticipant(Long userId, Long conversationId) {
        Optional<Participant> participant = participantRepository.findByUserIdAndConversationId(userId,conversationId);
        if (participant.isPresent()) return true;
        return false;
    }

    @Override
    public List<MessageDTO> getMsgsByConversationId(Long conversationId) {
        List<Message> messageList = messageRepository.findByConversationId(conversationId);
        List<MessageDTO> messageDTOList =  messageMapper.messageListToDTO(messageList);
        for (MessageDTO messageDTO:
             messageDTOList) {
            messageDTO.setNickname(
                    participantRepository.findByUserIdAndConversationId(
                            messageDTO.getUserId(),
                            messageDTO.getConversationId()
                    ).get().getNickName()
            );
        }
        return messageDTOList;
    }
}




