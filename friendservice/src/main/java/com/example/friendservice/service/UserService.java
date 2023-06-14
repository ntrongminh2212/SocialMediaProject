package com.example.friendservice.service;

import com.example.friendservice.entity.ResetPasswordToken;
import com.example.friendservice.entity.User;
import com.example.friendservice.entity.VerificationToken;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    User findByEmailAndPhoneNum(String email, String phoneNum);

    User save(User user);

    Optional<User> findByEmailOrPhoneNum(String email, String phoneNum);

    void saveVerificationToken(VerificationToken verificationToken);

    int updateUserEnableById(Long userId);

    void deleteVerificationToken(VerificationToken verificationToken);

    User findByEmail(String email);

    void saveResetPasswordToken(ResetPasswordToken resetPasswordToken);

    ResetPasswordToken findResetPasswordTokenByToken(String token);

    VerificationToken findVerificationTokenByToken(String token);

    void deleteResetPasswordToken(ResetPasswordToken resetPasswordToken);

    Optional<User> findById(Long userId);

    List<User> findAllById(Set<Long> lstUserId);

    List<User> findBySearchString(String upperCase);

    List<User> findFriendByUserId(Long userId);

    List<User> findAll(PageRequest limit);
}
