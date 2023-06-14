package com.example.friendservice.service;

import com.example.friendservice.dto.*;
import com.example.friendservice.entity.Friend;
import com.example.friendservice.feignclient.MessageClient;
import com.example.friendservice.mapper.FriendMapper;
import com.example.friendservice.mapper.UserMapper;
import com.example.friendservice.repository.FriendRepository;
import com.example.friendservice.repository.UserRepository;
import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Optional<Friend> findById(Long sourceId, Long targetId) {
        return friendRepository.findById(sourceId, targetId);
    }

    @Override
    public Friend save(Friend friend) {
        return friendRepository.save(friend);
    }

    @Override
    public int acceptFriend(Long sourceId, Long targetId) {
        return friendRepository.acceptFriend(sourceId, targetId);
    }
}
