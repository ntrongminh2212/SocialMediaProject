package com.example.friendservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostReactionDTO {
    private Long userId;
    private Long postId;
    private String firstName;
    private String lastName;
    private String avatar;
    private String reaction;
    private Date reactedTime;
}
