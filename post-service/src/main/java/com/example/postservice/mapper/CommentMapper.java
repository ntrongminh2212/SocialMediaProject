package com.example.postservice.mapper;

import com.example.postservice.dto.CommentDTO;
import com.example.postservice.entity.Comment;
import com.example.postservice.entity.CommentReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CommentReactionMapper.class})
public interface CommentMapper {
    @Mapping(source = "comment.post.postId",target = "postId")
    @Mapping(source = "comment.createTime", target = "createTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "comment.updateTime", target = "updateTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "comment.commentReactions", target = "commentReactionCount",qualifiedByName = "commentReactionCount")
    @Mapping(source = "comment.commentReactions",target = "commentReactionDTOList")
    CommentDTO commentToDTO(Comment comment);
    @Mapping(source = "commentDTO.postId",target = "post.postId")
    @Mapping(source = "commentDTO.createTime", target = "createTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "commentDTO.updateTime", target = "updateTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    Comment commentToEntity(CommentDTO commentDTO);

    List<CommentDTO> commentListToDTO(List<Comment> commentList);

    @Named("commentReactionCount")
    static int commentReactionCount(List<CommentReaction> list){
        return list.size();
    }
}
