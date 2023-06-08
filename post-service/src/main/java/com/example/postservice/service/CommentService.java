package com.example.postservice.service;

import com.example.postservice.dto.CommentDTO;
import com.example.postservice.dto.CommentReactionDTO;
import com.example.postservice.entity.Comment;

import java.util.List;

public interface CommentService{
    CommentDTO sendComment(CommentDTO commentDTO);

    CommentDTO modifyComment(CommentDTO commentDTO);

    boolean deleteComment(CommentDTO commentDTO);

    CommentReactionDTO reactToComment(CommentReactionDTO reactionDTO);

    List<Comment> findByUserId(Long userId);
}
