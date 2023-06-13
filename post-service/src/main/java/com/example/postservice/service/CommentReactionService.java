package com.example.postservice.service;

import com.example.postservice.entity.CommentReaction;
import java.util.List;
import java.util.Optional;

public interface CommentReactionService {
    List<CommentReaction> findByCommentReactionIdUserId(Long userId);

    List<CommentReaction> findByCommentId(Long commentId);

    void deleteAll(List<CommentReaction> commentReactionList);

    Optional<CommentReaction> findById(Long commentId, Long userId);

    CommentReaction save(CommentReaction commentReaction);
}
