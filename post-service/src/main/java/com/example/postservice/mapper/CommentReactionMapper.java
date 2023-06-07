package com.example.postservice.mapper;

import com.example.postservice.dto.CommentReactionDTO;
import com.example.postservice.entity.CommentReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentReactionMapper {
    @Mapping(source = "commentReaction.commentReactionId.userId",target = "userId")
    @Mapping(source = "commentReaction.commentReactionId.comment.commentId",target = "commentId")
    @Mapping(source = "commentReaction.reactedTime", target = "reactedTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    CommentReactionDTO commentReactionToDTO(CommentReaction commentReaction);
    @Mapping(source = "commentReactionDTO.userId",target = "commentReactionId.userId")
    @Mapping(source = "commentReactionDTO.commentId",target = "commentReactionId.comment.commentId")
    @Mapping(source = "commentReactionDTO.reactedTime", target = "reactedTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    CommentReaction commentReactionToEntity(CommentReactionDTO commentReactionDTO);
    List<CommentReactionDTO> commentReactionListToDTO(List<CommentReaction> commentReactionList);
}
