package com.example.postservice.entity;

import com.example.postservice.id.CommentReactionId;
import com.example.postservice.id.PostReactionId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "tbl_comment_reaction"
)
public class CommentReaction {
    @EmbeddedId
    CommentReactionId commentReactionId;
    private String reaction;
    @Column(name = "reacted_time")
    @CreationTimestamp
    private Date reactedTime;
}
