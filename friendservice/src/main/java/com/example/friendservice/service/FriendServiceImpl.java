package com.example.friendservice.service;

import com.example.friendservice.dto.FriendDTO;
import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.Friend;
import com.example.friendservice.entity.User;
import com.example.friendservice.mapper.FriendMapper;
import com.example.friendservice.mapper.UserMapper;
import com.example.friendservice.repository.FriendRepository;
import com.example.friendservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FriendServiceImpl implements FriendService{

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public FriendDTO requestFriend(Map<String,String> body) {
        FriendDTO friendDTO = new FriendDTO(
                Long.parseLong(body.get("userId")),
                Long.parseLong(body.get("targetId"))
        );
        Friend friend = friendMapper.friendToEntity(friendDTO);
        return friendMapper.friendToDTO(friendRepository.save(friend));
    }

    @Override
    public int acceptFriendRequest(Map<String, String> body) {
        Long sourceId = Long.parseLong(body.get("sourceId"));
        Long targetId = Long.parseLong(body.get("userId"));

        return friendRepository.acceptFriend(sourceId,targetId);
    }

    @Override
    public List<UserDTO> getFriendList(Long userId) {
        List<User> friendList = userRepository.findFriendByUserId(userId);
        return userMapper.userListToDTO(friendList);
    }
}
