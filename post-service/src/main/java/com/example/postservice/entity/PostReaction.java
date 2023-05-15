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
@IdClass(PostReactionId.class)
@Table(
        name = "tbl_post_reaction"
)
public class PostReaction {
    @Id
    @Column(name = "post_reaction_id")
    private Long postReactionId;
    @Id
    private Long userId;

    private String reaction;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @PrimaryKeyJoinColumn(
            name = "post_reaction_id",
            referencedColumnName = "post_id"
    )
    private Post post;
}
