package com.example.postservice.mapper;

import com.example.postservice.dto.PostReactionDTO;
import com.example.postservice.entity.PostReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostReactionMapper {
    @Mapping(source = "postReaction.postReactionId.userId",target = "userId")
    @Mapping(source = "postReaction.postReactionId.post.postId",target = "postId")
    @Mapping(source = "postReaction.reactedTime", target = "reactedTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    PostReactionDTO postReactionToDTO(PostReaction postReaction);
    @Mapping(source = "postReactionDTO.userId",target = "postReactionId.userId")
    @Mapping(source = "postReactionDTO.postId",target = "postReactionId.post.postId")
    @Mapping(source = "postReactionDTO.reactedTime", target = "reactedTime", dateFormat = "dd-MM-yyyy HH:mm:ss")
    PostReaction postReactionToEntity(PostReactionDTO postReactionDTO);
    List<PostReactionDTO> postReactionListToDTO(List<PostReaction> postReactionList);
}
