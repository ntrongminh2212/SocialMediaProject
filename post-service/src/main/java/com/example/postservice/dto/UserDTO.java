package com.example.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;
    private String avatar;
    private boolean sex;
    private LocalDate birthday;
    private Role role;
}
