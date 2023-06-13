package com.example.postservice.service;

import com.example.postservice.entity.Comment;
import com.example.postservice.mapper.CommentMapper;
import com.example.postservice.mapper.CommentReactionMapper;
import com.example.postservice.repository.CommentReactionRepository;
import com.example.postservice.repository.CommentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentReactionRepository reactionRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentReactionMapper commentReactionMapper;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> findByCommentIdAndUserId(Long commentId, Long userId) {
        return commentRepository.findByCommentIdAndUserId(commentId, userId);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> findByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }
}
