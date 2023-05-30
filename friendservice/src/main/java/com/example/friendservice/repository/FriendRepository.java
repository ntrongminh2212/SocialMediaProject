package com.example.friendservice.repository;

import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.Friend;
import com.example.friendservice.idkey.FriendId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE friend SET is_accepted= 'true' WHERE source_id = ?1 AND target_id = ?2 ",
            nativeQuery = true
    )
    int acceptFriend(Long sourceId, Long targetId);
}
