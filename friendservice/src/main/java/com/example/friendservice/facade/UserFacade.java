package com.example.friendservice.facade;

import com.example.friendservice.dto.AuthDTO;
import com.example.friendservice.dto.RegisterUserDTO;
import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.ResetPasswordToken;
import com.example.friendservice.entity.Role;
import com.example.friendservice.entity.User;
import com.example.friendservice.entity.VerificationToken;
import com.example.friendservice.mapper.UserMapper;
import com.example.friendservice.service.UserService;
import com.example.friendservice.util.JwtTokenUtil;
import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserFacade {

    private static Logger logger = Logger.getLogger(UserFacade.class);

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

        User user = userService.findByEmailOrPhoneNum(_user.getEmail(), _user.getPhoneNum()).get();
        if (Optional.ofNullable(user).isPresent() && passwordEncoder.matches(_user.getPassword(), user.getPassword())) {

            String accessToken = jwtTokenUtil.generateAccessToken(user);

            logger.info("[access token] " + accessToken);
            return Optional.ofNullable(new AuthDTO(user.getEmail(), accessToken));
        }
        return Optional.empty();
    }

    public void saveUserVerificationToken(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        userService.saveVerificationToken(verificationToken);
    }

    public String verifyRegistration(String token, String newToken) {
        Optional<VerificationToken> vt = Optional.ofNullable(userService.findVerificationTokenByToken(token));
        if (vt.isEmpty()) {
            return "Verify token invalid or the account which this token belong to has already been verified";
        }

        VerificationToken verificationToken = vt.get();
        User user = verificationToken.getUser();

        if (verificationToken.getExpirationTime().getTime() - (new Date().getTime()) <= 0) {
            verificationToken.setToken(newToken);
            verificationToken.setExpirationTime();
            userService.saveVerificationToken(verificationToken);
            // saveUserVerificationToken(newToken,user, verifyURL);

            return "Token Expired! A new verification token has been created.";
        }
        logger.info("[User Id]: " + user.getUserId());
        Long userId = user.getUserId();
        int rs = userService.updateUserEnableById(userId);
        if (rs > 0) {
            userService.deleteVerificationToken(verificationToken);
        }
        return "Account verified!";
    }

    public String saveResetPasswordToken(String email, String token) {
        Optional<User> user = Optional.ofNullable(userService.findByEmail(email));
        if (user.isEmpty()) {
            return "Email address not exist!";
        }
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(token, user.get());
        userService.saveResetPasswordToken(resetPasswordToken);
        return "Reset password token created";
    }

    public String changePassword(String newPassword, String token) {
        Optional<ResetPasswordToken> rpt = Optional.ofNullable(userService.findResetPasswordTokenByToken(token));
        if (rpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Change password token invalid");
        }

        User user = rpt.get().getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);
        userService.deleteResetPasswordToken(rpt.get());
        return "Password changed successfully!";
    }

    public User getUserByVerificationToken(String _token) {
        VerificationToken token = userService.findVerificationTokenByToken(_token);

        return token.getUser();
    }

    public Optional<User> getUserById(Long userId) {
        return userService.findById(userId);
    }

    public Optional<UserDTO> getUserInfo(Long userId) {
        Optional<User> user = getUserById(userId);
        if (user.isPresent()) {
            return Optional.ofNullable(userMapper.userToUserDTO(user.get()));
        }
        return Optional.empty();
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
