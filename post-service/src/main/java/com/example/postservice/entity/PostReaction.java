package com.example.postservice.entity;

import com.example.postservice.id.PostReactionId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "tbl_post_reaction"
)
public class PostReaction {
    @EmbeddedId
    PostReactionId postReactionId;
    private String reaction;
}
