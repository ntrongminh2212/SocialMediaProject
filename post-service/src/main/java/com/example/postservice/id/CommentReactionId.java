package com.example.postservice.id;

import com.example.postservice.entity.Comment;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CommentReactionId {
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
    private Long userId;
}
