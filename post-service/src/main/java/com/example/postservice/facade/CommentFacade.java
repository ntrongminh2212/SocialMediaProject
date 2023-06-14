package com.example.postservice.facade;

import com.example.postservice.dto.CommentDTO;
import com.example.postservice.dto.CommentReactionDTO;
import com.example.postservice.entity.Comment;
import com.example.postservice.entity.CommentReaction;
import com.example.postservice.entity.Post;
import com.example.postservice.mapper.CommentMapper;
import com.example.postservice.mapper.CommentReactionMapper;
import com.example.postservice.service.CommentReactionService;
import com.example.postservice.service.CommentService;
import java.util.List;
import java.util.Optional;

import com.example.postservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CommentFacade {
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

    @Autowired
    private CommentReactionService reactionService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentReactionMapper commentReactionMapper;

    public CommentDTO sendComment(CommentDTO commentDTO) {
        Optional<Post> post=postService.findById(commentDTO.getPostId());
        if (post.isPresent()) {
            Comment comment = commentMapper.commentToEntity(commentDTO);
            commentService.save(comment);
            return commentMapper.commentToDTO(comment);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public CommentDTO modifyComment(CommentDTO commentDTO) {
        Optional<Comment> comment =
                commentService.findByCommentIdAndUserId(commentDTO.getCommentId(), commentDTO.getUserId());
        if (comment.isPresent()) {
            comment.get().setContent(commentDTO.getContent());
            commentService.save(comment.get());
        }
        return commentMapper.commentToDTO(comment.get());
    }

    public boolean deleteComment(CommentDTO commentDTO) {
        Optional<Comment> comment =
                commentService.findByCommentIdAndUserId(commentDTO.getCommentId(), commentDTO.getUserId());
        if (comment.isPresent()) {
            reactionService.deleteAll(
                    reactionService.findByCommentId(comment.get().getCommentId()));
            commentService.delete(comment.get());
            return true;
        }
        return false;
    }

    public CommentReactionDTO reactToComment(CommentReactionDTO reactionDTO) {
        Optional<Comment> comment = commentService.findById(reactionDTO.getCommentId());
        if (comment.isPresent()) {
            Optional<CommentReaction> commentReactionOptional =
                    reactionService.findById(reactionDTO.getCommentId(), reactionDTO.getUserId());
            if (commentReactionOptional.isEmpty()) {
                CommentReaction commentReaction = commentReactionMapper.commentReactionToEntity(reactionDTO);
                return commentReactionMapper.commentReactionToDTO(reactionService.save(commentReaction));
            }
            return commentReactionMapper.commentReactionToDTO(commentReactionOptional.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
