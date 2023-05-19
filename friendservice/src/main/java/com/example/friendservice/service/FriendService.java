package com.example.friendservice.service;

import com.example.friendservice.dto.FriendDTO;

import java.util.Map;

public interface FriendService {
    FriendDTO requestFriend(Map<String,String> body);

    int acceptFriendRequest(Map<String, String> body);
}
