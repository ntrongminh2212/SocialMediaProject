package com.example.postservice.service;

import com.example.postservice.entity.CommentReaction;
import com.example.postservice.repository.CommentReactionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentReactionServiceImpl implements CommentReactionService {
    @Autowired
    private CommentReactionRepository reactionRepository;

    @Override
    public List<CommentReaction> findByCommentReactionIdUserId(Long userId) {
        return reactionRepository.findByCommentReactionIdUserId(userId);
    }

    @Override
    public List<CommentReaction> findByCommentId(Long commentId) {
        return reactionRepository.findByCommentId(commentId);
    }

    @Override
    public void deleteAll(List<CommentReaction> commentReactionList) {
        reactionRepository.deleteAll(commentReactionList);
    }

    @Override
    public Optional<CommentReaction> findById(Long commentId, Long userId) {
        return reactionRepository.findById(commentId, userId);
    }

    @Override
    public CommentReaction save(CommentReaction commentReaction) {
        return reactionRepository.save(commentReaction);
    }
}
