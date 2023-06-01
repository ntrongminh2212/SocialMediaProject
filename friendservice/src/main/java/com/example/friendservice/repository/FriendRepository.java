package com.example.friendservice.repository;

import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.Friend;
import com.example.friendservice.idkey.FriendId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE friend SET is_accepted= 'true' WHERE source_id = ?1 AND target_id = ?2 AND is_accepted = 'false'",
            nativeQuery = true
    )
    int acceptFriend(Long sourceId, Long targetId);

    @Query(
            value = "SELECT * FROM friend \n" +
                    "WHERE (source_id = ?1 AND target_id = ?2) \n" +
                    "OR (source_id = ?2 AND target_id = ?1)",
            nativeQuery = true
    )
    Optional<Friend> findById(Long sourceId, Long targetId);
}
