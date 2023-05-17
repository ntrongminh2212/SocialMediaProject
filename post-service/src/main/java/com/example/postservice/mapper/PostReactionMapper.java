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
    PostReactionDTO postReactionToPostReactionDTO(PostReaction postReaction);

    List<PostReactionDTO> getLstPostReactionDTO(List<PostReaction> postReactionList);
}
