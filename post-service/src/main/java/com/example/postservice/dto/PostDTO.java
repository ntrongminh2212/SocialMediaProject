package com.example.postservice.dto;

import com.example.postservice.entity.PostReaction;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long postId;
    private Long userId;
    private String statusContent;
    private String attachmentUrl;
    private List<PostReactionDTO> postReactions;
    private List<CommentDTO> comments;
}
