package com.example.postservice.dto;

import com.example.postservice.entity.PostReaction;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    private UserDTO user;
    private Long postId;
    private Long userId;
    private String statusContent;
    private String attachmentUrl;
    private String createdTime;
    private String updatedTime;
    private List<PostReactionDTO> postReactions;
    private List<CommentDTO> comments;
    private int postReactionsCount;
    private int commentsCount;
}
