package com.example.friendservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
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
