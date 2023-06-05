package com.example.postservice.dto;

import com.example.postservice.entity.PostReaction;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private UserDTO user;
    private Long postId;
    private Long userId;
    private String statusContent;
    private String attachmentUrl;
    private Date createdTime;
    private Date updatedTime;
    private List<PostReactionDTO> postReactions;
    private List<CommentDTO> comments;
}
