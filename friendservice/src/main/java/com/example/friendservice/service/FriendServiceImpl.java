package com.example.friendservice.service;

import com.example.friendservice.dto.*;
import com.example.friendservice.entity.Friend;
import com.example.friendservice.entity.User;
import com.example.friendservice.feignclient.MessageClient;
import com.example.friendservice.mapper.FriendMapper;
import com.example.friendservice.mapper.UserMapper;
import com.example.friendservice.repository.FriendRepository;
import com.example.friendservice.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FriendServiceImpl implements FriendService {

    Logger logger = Logger.getLogger(FriendServiceImpl.class);
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageClient messageClient;

    @Override
    public Optional<FriendDTO> requestFriend(Map<String, String> body) {
        FriendDTO friendDTO = new FriendDTO(
                Long.parseLong(body.get("userId")),
                Long.parseLong(body.get("targetId"))
        );
        Optional<Friend> friendRequest = friendRepository.findById(friendDTO.getSourceId(),friendDTO.getTargetId());
        if (friendRequest.isEmpty()) {
            Friend friend = friendMapper.friendToEntity(friendDTO);
            return Optional.ofNullable(friendMapper.friendToDTO(friendRepository.save(friend)));
        }
        return Optional.empty();
    }

    @Override
    public int acceptFriendRequest(Map<String, String> body) {
        Long sourceId = Long.parseLong(body.get("sourceId"));
        Long targetId = Long.parseLong(body.get("userId"));

        int rs = friendRepository.acceptFriend(sourceId, targetId);
        logger.info("[Result]: "+rs);
        if (rs > 0) {
            Optional<User> sourceUser = userRepository.findById(sourceId);
            Optional<User> targetUser = userRepository.findById(targetId);
            String conversationName = sourceUser.get().getFullName() + "-" + targetUser.get().getFullName();
            ConversationDTO conversationDTO = new ConversationDTO();
            conversationDTO.setUserId(targetId);
            conversationDTO.setName(conversationName);

            conversationDTO = messageClient.createConversation(conversationDTO).getBody();

            if (conversationDTO != null) {
                List<ParticipantDTO> participantDTOList = new ArrayList<>();
                participantDTOList.add(new ParticipantDTO(
                        conversationDTO.getConversationId(),
                        targetUser.get().getUserId(),
                        targetUser.get().getFullName()
                ));
                participantDTOList.add(new ParticipantDTO(
                        conversationDTO.getConversationId(),
                        sourceUser.get().getUserId(),
                        sourceUser.get().getFullName()
                ));

                messageClient.participateInConversation(new InviteFormDTO(
                        conversationDTO.getUserId(),
                        participantDTOList
                ));
            }
        }
        return rs;
    }

    @Override
    public List<UserDTO> getFriendList(Long userId) {
        List<User> friendList = userRepository.findFriendByUserId(userId);
        return userMapper.userListToDTO(friendList);
    }
}
