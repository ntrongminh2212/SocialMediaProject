package com.example.postservice.mapper;

import com.example.postservice.dto.PostDTO;
import com.example.postservice.dto.PostReactionDTO;
import com.example.postservice.dto.UserDTO;
import com.example.postservice.entity.Post;
import com.example.postservice.entity.PostReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {
    @Mapping(source = "userDTO.firstName",target = "firstName")
    @Mapping(source = "userDTO.lastName",target = "lastName")
    @Mapping(source = "userDTO.avatar",target = "avatar")
    @Mapping(source = "postReaction.reaction",target = "reaction")
    PostReactionDTO userAndPostToPostReactionDTO(UserDTO userDTO, PostReaction postReaction);

    @Mapping(source = "lstPostReactionDTO",target = "postReactions")
    PostDTO postToDTO(Post post, List<PostReactionDTO> lstPostReactionDTO);
    PostDTO postToDTO(Post post);
    Post postToEntity(PostDTO postDTO);
}







