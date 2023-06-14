package com.example.friendservice.service;

import com.example.friendservice.entity.Friend;
import java.util.Optional;

public interface FriendService {

    Optional<Friend> findById(Long sourceId, Long targetId);

    Friend save(Friend friend);

    int acceptFriend(Long sourceId, Long targetId);
}
