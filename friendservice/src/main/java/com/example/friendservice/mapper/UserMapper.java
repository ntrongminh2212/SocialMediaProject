package com.example.friendservice.mapper;

import com.example.friendservice.dto.PostReactionDTO;
import com.example.friendservice.dto.RegisterUserDTO;
import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.Role;
import com.example.friendservice.entity.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {
    @Autowired
    PasswordEncoder passwordEncoder;

    public abstract UserDTO  userToUserDTO(User user);
    @Mapping(source = "user.password",target = "password",qualifiedByName = "encodePassword")
    @Mapping(target = "role", constant = "USER")
    public abstract User registerFormToUser(RegisterUserDTO user);
    public abstract User userDTOToUser(UserDTO userDTO);
    public abstract PostReactionDTO updatePostReactionDTO(@MappingTarget PostReactionDTO postReactionDTO, User user);

    public abstract List<UserDTO> userListToDTO(List<User> userList);

    @Named("encodePassword")
    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
}
