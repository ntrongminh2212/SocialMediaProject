package com.example.friendservice.mapper;

import com.example.friendservice.dto.PostReactionDTO;
import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);
    PostReactionDTO updatePostReactionDTO(@MappingTarget PostReactionDTO postReactionDTO, User user);

    List<UserDTO> userListToDTO(List<User> userList);
}
