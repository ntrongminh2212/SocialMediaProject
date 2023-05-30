package com.example.friendservice.service;

import com.example.friendservice.dto.FriendDTO;
import com.example.friendservice.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface FriendService {
    FriendDTO requestFriend(Map<String,String> body);

    int acceptFriendRequest(Map<String, String> body);

    List<UserDTO> getFriendList(Long userId);
}
