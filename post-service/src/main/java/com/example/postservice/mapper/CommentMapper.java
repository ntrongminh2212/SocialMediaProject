package com.example.postservice.mapper;

import com.example.postservice.dto.CommentDTO;
import com.example.postservice.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    @Mapping(source = "comment.post.postId",target = "postId")
    CommentDTO commentToDTO(Comment comment);
    @Mapping(source = "commentDTO.postId",target = "post.postId")
    Comment commentToEntity(CommentDTO commentDTO);

    List<CommentDTO> commentListToDTO(List<Comment> commentList);
}
