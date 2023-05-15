package com.example.friendservice.controller;

import com.example.friendservice.dto.*;
import com.example.friendservice.entity.User;
import com.example.friendservice.mapper.UserMapper;
import com.example.friendservice.service.SendEmailService;
import com.example.friendservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    private static Logger logger = Logger.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private SendEmailService sendEmailService;

    @PostMapping("/register")
    public String registerUser(@RequestBody RegisterUserDTO _user, final HttpServletRequest request) {
        Optional<User> user = userService.registerUser(_user);
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            String verifyURL = applicationURL(request) + "/user/verifyRegistration?token=" + token;
            userService.saveUserVerificationToken(token, user.get());

            sendEmailService.sendSimpleEmail(user.get().getEmail().trim(),
                    "Account verification",
                    "Click the link to verify your account: \n" + verifyURL
                    );
            //logger.info("Click the link to verify your account: " + verifyURL);
            return "Success";
        }
        return "This email or phone number has already use in account";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam String token, final HttpServletRequest request) {
        String newToken = UUID.randomUUID().toString();
        String verifyURL = applicationURL(request) + "/user/verifyRegistration?token=" + newToken;
        String response = userService.verifyRegistration(token, newToken);
        if (response.compareTo("Token Expired! A new verification token has been created")==0){
            User user = userService.getUserByVerificationToken(newToken);

            sendEmailService.sendSimpleEmail(user.getEmail().trim(),
                    "Account verification",
                    "Click the link to verify your account: \n" + verifyURL
            );
        }
        return response;
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody EmailAddressDTO email, final HttpServletRequest request) {
        String token = UUID.randomUUID().toString();
        String verifyPasswordURL = applicationURL(request) + "/user/change-password?token=" + token;
        String rs = userService.saveResetPasswordToken(email.getEmail(), token);
        if (rs.compareTo("Reset password token created") == 0) {
            logger.info("Link to change password: " + verifyPasswordURL);
        }
        return rs;
    }

    @PostMapping("/change-password")
    public String resetPassword(@RequestParam String token, @RequestBody NewPasswordDTO newPassword) {
        String rs = userService.changePassword(newPassword.getNewPassword(), token);
        return rs;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginDTO loginDTO) {
        User user = new User();
        user.setEmail(loginDTO.getAccountName());
        user.setPhoneNum(loginDTO.getAccountName());
        user.setPassword(loginDTO.getPassword());
        return userService.login(user)
                .orElse(new AuthResponse());
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/info/{user_id}")
    public Optional<UserDTO> getUserInfo(@PathVariable("user_id") Long user_id){
        Optional<UserDTO> user = userService.getUserInfo(user_id);
        if (user.isPresent()){
            return user;
        }
        return Optional.empty();
    }
    private String applicationURL(HttpServletRequest request) {
        return "http://" + request.getServerName()
                + ":" + request.getServerPort()
                + request.getContextPath();
    }
}










