package com.example.friendservice.service;

import com.example.friendservice.entity.ResetPasswordToken;
import com.example.friendservice.entity.User;
import com.example.friendservice.entity.VerificationToken;
import com.example.friendservice.repository.ResetPasswordTokenRepository;
import com.example.friendservice.repository.UserRepository;
import com.example.friendservice.repository.VerificationTokenRepository;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Override
    public User findByEmailAndPhoneNum(String email, String phoneNum) {
        return userRepository.findByEmailAndPhoneNum(email, phoneNum);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmailOrPhoneNum(String email, String phoneNum) {
        return userRepository.findByEmailOrPhoneNum(email, phoneNum);
    }

    @Override
    public void saveVerificationToken(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public int updateUserEnableById(Long userId) {
        return userRepository.updateUserEnableById(userId);
    }

    @Override
    public void deleteVerificationToken(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveResetPasswordToken(ResetPasswordToken resetPasswordToken) {
        resetPasswordTokenRepository.save(resetPasswordToken);
    }

    @Override
    public ResetPasswordToken findResetPasswordTokenByToken(String token) {
        return resetPasswordTokenRepository.findByToken(token);
    }

    @Override
    public VerificationToken findVerificationTokenByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void deleteResetPasswordToken(ResetPasswordToken resetPasswordToken) {
        resetPasswordTokenRepository.delete(resetPasswordToken);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findAllById(Set<Long> lstUserId) {
        return userRepository.findAllById(lstUserId);
    }

    @Override
    public List<User> findBySearchString(String upperCase) {
        return userRepository.findBySearchString(upperCase);
    }

    @Override
    public List<User> findFriendByUserId(Long userId) {
        return userRepository.findFriendByUserId(userId);
    }

    @Override
    public List<User> findAll(PageRequest limit) {
        return userRepository.findAll(limit).toList();
    }
}
