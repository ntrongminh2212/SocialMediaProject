package com.example.friendservice.facade;

import com.example.friendservice.configuration.Constants;
import com.example.friendservice.dto.AuthDTO;
import com.example.friendservice.dto.RegisterUserDTO;
import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.ResetPasswordToken;
import com.example.friendservice.entity.User;
import com.example.friendservice.entity.VerificationToken;
import com.example.friendservice.mapper.UserMapper;
import com.example.friendservice.service.UserService;
import com.example.friendservice.util.JwtTokenUtil;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class UserFacade {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserMapper userMapper;

    public Optional<User> registerUser(RegisterUserDTO _user) {
        User user = userMapper.registerFormToUser(_user);
        if (_user.getPassword().compareTo(_user.getConfirmPassword())!=0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Confirm password not match");
        }
        Optional<User> existUser = userService.findByEmailOrPhoneNum(user.getEmail(), user.getPhoneNum());
        if (existUser.isPresent()) {
            return Optional.empty();
        }
        return Optional.ofNullable(userService.save(user));
    }

    public Optional<AuthDTO> login(User _user) {
        Optional<User> userOptional = userService.findByEmailOrPhoneNum(_user.getEmail(), _user.getPhoneNum());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(_user.getPassword(), user.getPassword())) {
                String accessToken = jwtTokenUtil.generateAccessToken(user);
                log.info("[access token] " + accessToken);
                return Optional.ofNullable(new AuthDTO(user.getEmail(), accessToken));
            }
        }
        return Optional.empty();
    }

    public void saveUserVerificationToken(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        userService.saveVerificationToken(verificationToken);
    }

    public String verifyRegistration(String token, String newToken) {
        Optional<VerificationToken> vt = userService.findVerificationTokenByToken(token);
        if (vt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Verify token invalid or the account which this token belong to has already been verified");
        }
        VerificationToken verificationToken = vt.get();
        User user = verificationToken.getUser();
        if (verificationToken.getExpirationTime().getTime() - (new Date().getTime()) <= 0) {
            verificationToken.setToken(newToken);
            verificationToken.setExpirationTime();
            userService.saveVerificationToken(verificationToken);
            return Constants.TOKEN_EXPIRE_MESSAGE;
        }
        log.info("[User Id]: " + user.getUserId());
        Long userId = user.getUserId();
        int rs = userService.updateUserEnableById(userId);
        if (rs > 0) {
            userService.deleteVerificationToken(verificationToken);
        }
        return Constants.ACCOUNT_VERIFIED_MESSAGE;
    }

    public String saveResetPasswordToken(String email, String token) {
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email address not exist!");
        }
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(token, user.get());
        userService.saveResetPasswordToken(resetPasswordToken);
        return Constants.RESET_PASSWORD_TOKEN_CREATED_MESSAGE;
    }

    public String changePassword(String newPassword, String token) {
        Optional<ResetPasswordToken> rpt = userService.findResetPasswordTokenByToken(token);
        if (rpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Change password token invalid");
        }
        User user = rpt.get().getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);
        userService.deleteResetPasswordToken(rpt.get());
        return Constants.PASSWORD_CHANGED_SUCCESSFULLY;
    }

    public User getUserByVerificationToken(String _token) {
        Optional<VerificationToken> token = userService.findVerificationTokenByToken(_token);
        if (token.isPresent()){
            return token.get().getUser();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    public UserDTO getUserInfo(Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            return userMapper.userToUserDTO(user.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    public Map<Long, UserDTO> getListUserDetail(Set<Long> lstUserId) {
        Map<Long, UserDTO> userDTOMap = new HashMap<>();
        List<UserDTO> userDTOList = userMapper.userListToDTO(userService.findAllById(lstUserId));

        for (UserDTO userDTO : userDTOList) {
            userDTOMap.put(userDTO.getUserId(), userDTO);
        }
        return userDTOMap;
    }
    public List<UserDTO> searchUsers(String searchStr) {
        List<UserDTO> userDTOList = userMapper.userListToDTO(userService.findBySearchString(searchStr.toUpperCase()));
        return userDTOList;
    }
}
