package com.example.postservice.mapper;

import com.example.postservice.dto.PostDTO;
import com.example.postservice.dto.PostReactionDTO;
import com.example.postservice.dto.UserDTO;
import com.example.postservice.entity.Comment;
import com.example.postservice.entity.Post;
import com.example.postservice.entity.PostReaction;
import jakarta.persistence.Column;
import org.hibernate.annotations.UpdateTimestamp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Date;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {PostReactionMapper.class, CommentMapper.class})
public interface PostMapper {
    @Mapping(source = "userDTO",target = "user")
    @Mapping(source = "postReaction.reaction",target = "reaction")
    PostReactionDTO userAndPostToPostReactionDTO(UserDTO userDTO, PostReaction postReaction);

    @Mapping(source = "lstPostReactionDTO",target = "postReactions")
    @Mapping(source = "post.creatorId",target = "userId")
    @Mapping(source = "post.createdTime", target = "createdTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "post.updatedTime", target = "updatedTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    PostDTO postToDTO(Post post, List<PostReactionDTO> lstPostReactionDTO);

    @Mapping(source = "post.creatorId",target = "userId")
    @Mapping(source = "post.createdTime", target = "createdTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "post.updatedTime", target = "updatedTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "post.postReactions", target = "postReactionsCount", qualifiedByName = "postReactionCount")
    @Mapping(source = "post.comments", target = "commentsCount", qualifiedByName = "commentCount")
    @Mapping(source = "post.postReactions", target = "postReactions")
    @Mapping(source = "post.comments", target = "comments", conditionExpression = "java(isMappingComment==true)" )
    PostDTO postToDTO(Post post, boolean isMappingComment);

    @Mapping(source = "postDTO.userId",target = "creatorId")
    @Mapping(source = "postDTO.createdTime", target = "createdTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "postDTO.updatedTime", target = "updatedTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    Post postToEntity(PostDTO postDTO);

    @Named(value = "postReactionCount")
    static int postReactionCount(List<PostReaction> list){
        return list.size();
    }

    @Named(value = "commentCount")
    static int commentCount(List<Comment> list){
        return list.size();
    }
}








