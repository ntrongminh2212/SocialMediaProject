package com.example.friendservice.service;

import com.example.friendservice.dto.AuthDTO;
import com.example.friendservice.dto.PostReactionDTO;
import com.example.friendservice.dto.RegisterUserDTO;
import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public Optional<User> registerUser(RegisterUserDTO user);
    public Optional<AuthDTO> login(User user);
    void saveUserVerificationToken(String token, User user);

    String verifyRegistration(String token,String newToken);

    String saveResetPasswordToken(String email, String token);

    String changePassword(String newPassword, String token);

    User getUserByVerificationToken(String token);

    Optional<User> getUserById(Long userId);

    Optional<UserDTO> getUserInfo(Long userId);

    List<PostReactionDTO> getUsersInfo(List<PostReactionDTO> lstPostReactionDTO);
}
