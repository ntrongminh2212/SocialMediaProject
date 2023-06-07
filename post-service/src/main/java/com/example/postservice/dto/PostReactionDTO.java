package com.example.postservice.dto;

import com.example.postservice.id.PostReactionId;
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
    private UserDTO user;
    private Long userId;
    private Long postId;
    private String reaction;
    private String reactedTime;
}
