package com.example.friendservice.facade;

import com.example.friendservice.dto.*;
import com.example.friendservice.entity.Friend;
import com.example.friendservice.entity.User;
import com.example.friendservice.feignclient.MessageClient;
import com.example.friendservice.mapper.FriendMapper;
import com.example.friendservice.mapper.UserMapper;
import com.example.friendservice.service.FriendService;
import com.example.friendservice.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FriendFacade {
    Logger logger = Logger.getLogger(FriendFacade.class);

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageClient messageClient;

    public Optional<FriendDTO> requestFriend(Map<String, String> body) {
        FriendDTO friendDTO = new FriendDTO(Long.parseLong(body.get("userId")), Long.parseLong(body.get("targetId")));
        Optional<Friend> friendRequest = friendService.findById(friendDTO.getSourceId(), friendDTO.getTargetId());
        if (friendRequest.isEmpty()) {
            Friend friend = friendMapper.friendToEntity(friendDTO);
            return Optional.ofNullable(friendMapper.friendToDTO(friendService.save(friend)));
        }
        return Optional.empty();
    }

    public int acceptFriendRequest(Map<String, String> body) {
        Long sourceId = Long.parseLong(body.get("sourceId"));
        Long targetId = Long.parseLong(body.get("userId"));

        int rs = friendService.acceptFriend(sourceId, targetId);
        logger.info("[Result]: " + rs);
        if (rs > 0) {
            Optional<User> sourceUser = userService.findById(sourceId);
            Optional<User> targetUser = userService.findById(targetId);
            String conversationName =
                    sourceUser.get().getFullName() + "-" + targetUser.get().getFullName();
            ConversationDTO conversationDTO = new ConversationDTO();
            conversationDTO.setUserId(targetId);
            conversationDTO.setName(conversationName);

            conversationDTO = messageClient.createConversation(conversationDTO).getBody();

            if (conversationDTO != null) {
                List<ParticipantDTO> participantDTOList = new ArrayList<>();
                participantDTOList.add(new ParticipantDTO(
                        conversationDTO.getConversationId(),
                        targetUser.get().getUserId(),
                        targetUser.get().getFullName()));
                participantDTOList.add(new ParticipantDTO(
                        conversationDTO.getConversationId(),
                        sourceUser.get().getUserId(),
                        sourceUser.get().getFullName()));

                messageClient.participateInConversation(
                        new InviteFormDTO(conversationDTO.getUserId(), participantDTOList));
            }
        }
        return rs;
    }

    public List<UserDTO> getFriendList(Long userId) {
        List<User> friendList = userService.findFriendByUserId(userId);
        return userMapper.userListToDTO(friendList);
    }
}
