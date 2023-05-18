package com.example.postservice.entity;

import com.example.postservice.id.PostReactionId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

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
    @Column(name = "reacted_time")
    @CreationTimestamp
    private Date reactedTime;
}
