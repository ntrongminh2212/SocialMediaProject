package com.example.postservice.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private UserDTO user;
    private Long commentId;
    private Long postId;
    private Long userId;
    private String content;
    private String createTime;
    private String updateTime;
    private List<CommentReactionDTO> commentReactionDTOList;
    private int commentReactionCount;
}
