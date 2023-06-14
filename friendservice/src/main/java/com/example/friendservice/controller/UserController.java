package com.example.friendservice.controller;

import com.example.friendservice.dto.*;
import com.example.friendservice.entity.User;
import com.example.friendservice.facade.UserFacade;
import com.example.friendservice.service.AuthenticationService;
import com.example.friendservice.service.SendEmailService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    private static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegisterUserDTO _user, final HttpServletRequest request) {
        Optional<User> user = userFacade.registerUser(_user);
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            String verifyURL = applicationURL(request) + "/user/verifyRegistration?verify=" + token;
            userFacade.saveUserVerificationToken(token, user.get());

            sendEmailService.sendSimpleEmail(
                    user.get().getEmail().trim(),
                    "Account verification",
                    "Click the link to verify your account: \n" + verifyURL);
            // logger.info("Click the link to verify your account: " + verifyURL);
            return ResponseEntity.ok(ResponseDTO.SUCCESS);
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "This email or phone number has already use in account");
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<Object> verifyRegistration(@RequestParam String verify, final HttpServletRequest request) {
        String newToken = UUID.randomUUID().toString();
        String verifyURL = applicationURL(request) + "/user/verifyRegistration?verify=" + newToken;
        String response = userFacade.verifyRegistration(verify, newToken);
        ResponseDTO.SUCCESS.put("message", response);
        if (response.compareTo("Token Expired! A new verification token has been created") == 0) {
            User user = userFacade.getUserByVerificationToken(newToken);
            sendEmailService.sendSimpleEmail(
                    user.getEmail().trim(),
                    "Account verification",
                    "Click the link to verify your account: \n" + verifyURL);
        }
        return ResponseEntity.ok(ResponseDTO.SUCCESS);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody EmailAddressDTO email, final HttpServletRequest request) {
        String token = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String verifyPasswordURL =
                applicationURL(request) + "/user/change-password?verify=" + token + "&password=" + password;
        String rs = userFacade.saveResetPasswordToken(email.getEmail(), token);
        if (rs.compareTo("Reset password token created") == 0) {
            logger.info("Link to change password: " + verifyPasswordURL);
        }
        ResponseDTO.SUCCESS.put("message", rs);
        return ResponseEntity.ok(ResponseDTO.SUCCESS);
    }

    @GetMapping("/verify-forgot-password")
    public ResponseEntity<Object> resetPassword(@RequestParam String verify, @RequestParam String password) {
        String rs = userFacade.changePassword(password, verify);
        ResponseDTO.SUCCESS.put("message", rs);
        return ResponseEntity.ok(ResponseDTO.SUCCESS);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTO> login(@RequestBody LoginDTO loginDTO) {
        User user = new User();
        logger.info(loginDTO);
        user.setEmail(loginDTO.getAccountName());
        user.setPhoneNum(loginDTO.getAccountName());
        user.setPassword(loginDTO.getPassword());

        Optional<AuthDTO> authDTO = userFacade.login(user);
        if (authDTO.isPresent()) {
            return ResponseEntity.ok(authDTO.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/authenticate")
    public Optional<UserDTO> authenticate(final HttpServletRequest request) {
        String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        Optional<UserDTO> userDTO = authenticationService.authenticateToken(authToken);
        if (userDTO.isPresent()) {
            logger.info(userDTO.get().toString());
            return userDTO;
        }

        return Optional.empty();
    }

    @GetMapping("/info")
    public ResponseEntity<UserDTO> getUserInfo(@RequestHeader Long userId, final HttpServletRequest request) {
        Optional<UserDTO> user = userFacade.getUserInfo(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/info/{ofUserId}")
    public ResponseEntity<UserDTO> getUserInfo(
            @RequestHeader Long userId, @PathVariable("ofUserId") Long ofUserId, final HttpServletRequest request) {
        Optional<UserDTO> user = userFacade.getUserInfo(ofUserId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/list-user-details")
    public Map<Long, UserDTO> getListUserDetail(@RequestBody Set<Long> lstUserId) {
        return userFacade.getListUserDetail(lstUserId);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String searchStr, @RequestHeader Long userId) {
        List<UserDTO> userDTOList = userFacade.searchUsers(searchStr);
        return ResponseEntity.ok(userDTOList);
    }

    private String applicationURL(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
