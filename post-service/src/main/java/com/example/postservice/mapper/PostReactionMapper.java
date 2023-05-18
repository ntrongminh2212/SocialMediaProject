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
    PostReactionDTO postReactionToDTO(PostReaction postReaction);
    @Mapping(source = "postReactionDTO.userId",target = "postReactionId.userId")
    @Mapping(source = "postReactionDTO.postId",target = "postReactionId.post.postId")
    PostReaction postReactionToEntity(PostReactionDTO postReactionDTO);
    List<PostReactionDTO> getLstPostReactionDTO(List<PostReaction> postReactionList);
}
