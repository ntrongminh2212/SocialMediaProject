package com.example.postservice.dto;

import com.example.postservice.entity.Comment;
import com.example.postservice.id.CommentReactionId;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReactionDTO {
    private UserDTO user;
    private Long commentId;
    private Long userId;
    private String reaction;
    private String reactedTime;
}
