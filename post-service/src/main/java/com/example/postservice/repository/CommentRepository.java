package com.example.postservice.repository;

import com.example.postservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByCommentIdAndUserId(Long commentId, Long userId);

    @Query(
            value = "SELECT* FROM tbl_comment WHERE post_id = ?1",
            nativeQuery = true
    )
    List<Comment> findByPostId(Long postId);

    List<Comment> findByUserId(Long userId);
}
