package com.example.friendservice.dto;

import com.example.friendservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDTO implements Serializable {
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
