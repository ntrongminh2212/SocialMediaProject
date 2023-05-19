package com.example.friendservice.mapper;

import com.example.friendservice.dto.FriendDTO;
import com.example.friendservice.entity.Friend;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FriendMapper {

    @Mapping(source = "friend.friendId.sourceId.userId",target = "sourceId")
    @Mapping(source = "friend.friendId.targetId.userId",target = "targetId")
    FriendDTO friendToDTO(Friend friend);

    @Mapping(source = "friendDTO.sourceId",target = "friendId.sourceId.userId")
    @Mapping(source = "friendDTO.targetId",target = "friendId.targetId.userId")
    Friend friendToEntity(FriendDTO friendDTO);
}
