package com.example.postservice.service;

import com.example.postservice.dto.CommentDTO;
import com.example.postservice.dto.CommentReactionDTO;
import com.example.postservice.entity.Comment;
import com.example.postservice.entity.CommentReaction;
import com.example.postservice.mapper.CommentMapper;
import com.example.postservice.mapper.CommentReactionMapper;
import com.example.postservice.repository.CommentReactionRepository;
import com.example.postservice.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentReactionRepository reactionRepository;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentReactionMapper commentReactionMapper;
    @Override
    public CommentDTO sendComment(CommentDTO commentDTO) {
        Comment comment = commentRepository.save(commentMapper.commentToEntity(commentDTO));
        return commentMapper.commentToDTO(comment);
    }

    @Override
    public CommentDTO modifyComment(CommentDTO commentDTO) {
        Optional<Comment> comment = commentRepository.findByCommentIdAndUserId(
                commentDTO.getCommentId(),
                commentDTO.getUserId()
        );
        if (comment.isPresent()){
            comment.get().setContent(commentDTO.getContent());
            commentRepository.save(comment.get());
        }
        return commentMapper.commentToDTO(comment.get());
    }

    @Override
    public boolean deleteComment(CommentDTO commentDTO) {
        Optional<Comment> comment = commentRepository.findByCommentIdAndUserId(
                commentDTO.getCommentId(),
                commentDTO.getUserId()
        );
        if (comment.isPresent()){
            commentRepository.delete(comment.get());
            return true;
        }
        return false;
    }

    @Override
    public CommentReactionDTO reactToComment(CommentReactionDTO reactionDTO) {
        CommentReaction comment = commentReactionMapper.commentReactionToEntity(reactionDTO);
        return commentReactionMapper.commentReactionToDTO(reactionRepository.save(comment));
    }
}
