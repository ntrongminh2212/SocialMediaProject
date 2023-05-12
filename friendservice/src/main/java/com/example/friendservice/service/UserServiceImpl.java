package com.example.friendservice.service;

import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.ResetPasswordToken;
import com.example.friendservice.entity.User;
import com.example.friendservice.entity.VerificationToken;
import com.example.friendservice.repository.ResetPasswordTokenRepository;
import com.example.friendservice.repository.UserRepository;
import com.example.friendservice.repository.VerificationTokenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private ResetPasswordTokenRepository resetPasswordTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Optional<User> registerUser(UserDTO _user) {
        User user = User.builder()
                .email(_user.getEmail())
                .phoneNum(_user.getPhoneNum())
                .firstName(_user.getFirstName())
                .lastName(_user.getLastName())
                .birthday(_user.getBirthday())
                .password(passwordEncoder.encode(_user.getPassword()))
                .sex(_user.isSex())
                .role("USER")
                .build();

        User existUser = userRepository.findByEmailAndPhoneNum(user.getEmail(), user.getPhoneNum());
        if (existUser != null) {
            return Optional.empty();
        }
        return Optional.ofNullable(userRepository.save(user));
    }

    @Override
    public Optional<User> login(User _user) {
        User user = userRepository.findByEmailOrPhoneNum(
                _user.getEmail(),
                _user.getPhoneNum()
        );
        if (Optional.ofNullable(user).isPresent() &&
                passwordEncoder.matches(_user.getPassword(), user.getPassword())) {
            return Optional.ofNullable(user);
        }
        return Optional.empty(        );
    }

    @Override
    public void saveUserVerificationToken(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String verifyRegistration(String token, String newToken) {
        Optional<VerificationToken> vt = Optional.ofNullable(verificationTokenRepository.findByToken(token));
        if (vt.isEmpty()) {
            return "Verify token invalid or the account which this token belong to has already been verified";
        }

        VerificationToken verificationToken = vt.get();
        User user = verificationToken.getUser();

        if (verificationToken.getExpirationTime().getTime()
                - (new Date().getTime()) <= 0) {
            verificationToken.setToken(newToken);
            verificationToken.setExpirationTime();
            verificationTokenRepository.save(verificationToken);
            //saveUserVerificationToken(newToken,user, verifyURL);

            return "Token Expired! A new verification token has been created.";
        }
        logger.info("[User Id]: " + user.getUserId());
        Long userId = user.getUserId();
        int rs = userRepository.updateUserEnableById(userId);
        if (rs > 0) {
            verificationTokenRepository.delete(verificationToken);
        }
        return "Account verified!";
    }

    @Override
    public String saveResetPasswordToken(String email, String token) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isEmpty()) {
            return "Email address not exist!";
        }
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(token, user.get());
        resetPasswordTokenRepository.save(resetPasswordToken);
        return "Reset password token created";
    }

    @Override
    public String changePassword(String newPassword, String token) {
        Optional<ResetPasswordToken> rpt =
                Optional.ofNullable(resetPasswordTokenRepository.findByToken(token));
        if (rpt.isEmpty()) {
            return "Change password token invalid";
        }

        User user = rpt.get().getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetPasswordTokenRepository.delete(rpt.get());
        return "Password changed successfully!";
    }

    @Override
    public User getUserByToken(String _token) {
        VerificationToken token = verificationTokenRepository.findByToken(_token);

        return token.getUser();
    }
}

















