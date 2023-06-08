package com.example.postservice.service;

import com.example.postservice.entity.CommentReaction;
import com.example.postservice.repository.CommentReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentReactionServiceImpl implements CommentReactionService{
    @Autowired
    private CommentReactionRepository reactionRepository;

    @Override
    public List<CommentReaction> findByCommentReactionIdUserId(Long userId) {
        return reactionRepository.findByCommentReactionIdUserId(userId);
    }
}
