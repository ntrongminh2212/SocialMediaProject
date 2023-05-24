package com.example.postservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "tbl_comment"
)
public class Comment {
    @Id
    @SequenceGenerator(
            name = "comment_sequence",
            sequenceName = "comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_sequence"
    )
    private Long commentId;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    private Long userId;
    private String content;
    @CreationTimestamp
    private Date createTime;
    @UpdateTimestamp
    private Date updateTime;
}