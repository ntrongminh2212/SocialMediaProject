package com.example.friendservice.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RegisterUserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;
    private String password;
    private String confirmPassword;
    private boolean sex;
    private LocalDate birthday;
    private String accessToken;
}
