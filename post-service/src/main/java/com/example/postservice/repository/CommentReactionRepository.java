package com.example.postservice.repository;

import com.example.postservice.entity.CommentReaction;
import com.example.postservice.id.CommentReactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, CommentReactionId> {

    @Query(
            value = "SELECT * FROM tbl_comment_reaction WHERE comment_id = ?1",
            nativeQuery = true
    )
    List<CommentReaction> findByCommentId(Long commentId);

    @Query(
            value = "SELECT * FROM tbl_comment_reaction WHERE comment_id = ?1 AND user_id = ?2",
            nativeQuery = true
    )
    Optional<CommentReaction> findById(Long commentId, Long userId);
}
