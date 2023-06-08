package com.example.postservice.service;

import com.example.postservice.entity.CommentReaction;

import java.util.List;

public interface CommentReactionService {
    List<CommentReaction> findByCommentReactionIdUserId(Long userId);
}
