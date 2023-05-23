package com.example.friendservice.controller;

import com.example.friendservice.dto.*;
import com.example.friendservice.entity.User;
import com.example.friendservice.service.AuthenticationService;
import com.example.friendservice.service.SendEmailService;
import com.example.friendservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    private static Logger logger = Logger.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private AuthenticationService authenticationService;

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
    public AuthDTO login(@RequestBody LoginDTO loginDTO) {
        User user = new User();
        logger.info(loginDTO);
        user.setEmail(loginDTO.getAccountName());
        user.setPhoneNum(loginDTO.getAccountName());
        user.setPassword(loginDTO.getPassword());
        return userService.login(user)
                .orElse(new AuthDTO());
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/authenticate")
    public ResponseEntity<UserDTO> authenticate(final HttpServletRequest request){
        String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        Optional<UserDTO> userDTO = authenticationService.authenticateToken(authToken);
        if (userDTO.isPresent()){
            logger.info(userDTO.get().toString());
            return ResponseEntity.ok(userDTO.get());
        }
        return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/info/{user_id}")
    public Optional<UserDTO> getUserInfo(@PathVariable("user_id") Long user_id){
        logger.info("[request come]");
        Optional<UserDTO> user = userService.getUserInfo(user_id);
        if (user.isPresent()){
            return user;
        }
        return Optional.empty();
    }

    @PostMapping("/reaction-details")
    public List<PostReactionDTO> getUserReactionDetail(@RequestBody List<PostReactionDTO> lstPostReactionDTO){
        return userService.getUserReactionDetail(lstPostReactionDTO);
    }

    private String applicationURL(HttpServletRequest request) {
        return "http://" + request.getServerName()
                + ":" + request.getServerPort()
                + request.getContextPath();
    }
}










