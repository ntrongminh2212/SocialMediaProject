package com.example.postservice.mapper;

import com.example.postservice.dto.*;
import com.example.postservice.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActivityMapper {
    @Mapping(source = "postDTO.updatedTime", target = "actionTime")
    @Mapping(source = "postDTO", target = "action", qualifiedByName = "postToObject")
    @Mapping(target = "activityType", constant ="POST")
    ActivityDTO postToActivityDTO(PostDTO postDTO);
    List<ActivityDTO> postToActivityDTO(List<PostDTO> postDTOList);

    @Mapping(source = "commentDTO.updateTime", target = "actionTime")
    @Mapping(source = "commentDTO", target = "action", qualifiedByName = "commentToObject")
    @Mapping(target = "activityType", constant ="COMMENT")
    ActivityDTO commentToActivityDTO(CommentDTO commentDTO);
    List<ActivityDTO> commentToActivityDTO(List<CommentDTO> commentDTOList);

    @Mapping(source = "postReactionDTO.reactedTime", target = "actionTime")
    @Mapping(source = "postReactionDTO", target = "action", qualifiedByName = "postReactionToObject")
    @Mapping(target = "activityType", constant ="REACTPOST")
    ActivityDTO postReactionToActivityDTO(PostReactionDTO postReactionDTO);
    List<ActivityDTO> postReactionToActivityDTO(List<PostReactionDTO> postReactionDTOList);

    @Mapping(source = "commentReactionDTO.reactedTime", target = "actionTime")
    @Mapping(source = "commentReactionDTO", target = "action", qualifiedByName = "commentReactionToObject")
    @Mapping(target = "activityType", constant ="REACTCOMMENT")
    ActivityDTO commentReactionToActivityDTO(CommentReactionDTO commentReactionDTO);
    List<ActivityDTO> commentReactionToActivityDTO(List<CommentReactionDTO> commentReactionDTOList);

    @Named("postToObject")
    static Object postToObject(PostDTO postDTO){
        Object object = postDTO;
        return object;
    }

    @Named("commentToObject")
    static Object commentToObject(CommentDTO commentDTO){
        Object object = commentDTO;
        return object;
    }

    @Named("postReactionToObject")
    static Object postReactionToObject(PostReactionDTO postReactionDTO){
        Object object = postReactionDTO;
        return object;
    }

    @Named("commentReactionToObject")
    static Object commentReactionToObject(CommentReactionDTO commentReactionDTO){
        Object object = commentReactionDTO;
        return object;
    }
}
