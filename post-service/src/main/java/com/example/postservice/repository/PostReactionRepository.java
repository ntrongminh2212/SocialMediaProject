package com.example.postservice.repository;

import com.example.postservice.entity.PostReaction;
import com.example.postservice.id.PostReactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, PostReactionId> {

    @Query(
            value = "SELECT *\n" +
                    "FROM tbl_post_reaction\n" +
                    "WHERE post_id = ?1",
            nativeQuery = true
    )
    Optional<List<PostReaction>> findByPost(Long postId);

    @Query(
            value = "SELECT *\n" +
                    "FROM tbl_post_reaction\n" +
                    "WHERE post_id = ?1 \n" +
                    "AND user_id = ?2",
            nativeQuery = true
    )
    Optional<PostReaction> findByPostIdAndUserId(Long postId, Long userId);
}
