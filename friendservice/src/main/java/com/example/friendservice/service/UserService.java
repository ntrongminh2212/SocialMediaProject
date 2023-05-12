package com.example.friendservice.service;

import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.User;

import java.util.Optional;

public interface UserService {
    public Optional<User> registerUser(UserDTO user);
    public Optional<User> login(User user);
    void saveUserVerificationToken(String token, User user);

    String verifyRegistration(String token,String newToken);

    String saveResetPasswordToken(String email, String token);

    String changePassword(String newPassword, String token);

    User getUserByToken(String token);
}
