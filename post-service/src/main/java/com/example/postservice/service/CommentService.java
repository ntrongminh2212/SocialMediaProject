package com.example.postservice.service;

import com.example.postservice.entity.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentService {

    Comment save(Comment comment);

    Optional<Comment> findByCommentIdAndUserId(Long commentId, Long userId);

    void delete(Comment comment);

    List<Comment> findByUserId(Long userId);

    Optional<Comment> findById(Long commentId);
}
