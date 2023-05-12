package com.example.friendservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name="tbl_user"
)
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long userId;
    private String firstName;
    private String lastName;

    @Column(
            name="email",
            unique = true,
            nullable = false
    )
    private String email;
    private String phoneNum;
    @Column(length = 60)
    private String password;
    private String role;
    private boolean sex;
    private LocalDate birthday;
    private boolean enable=false;
}
