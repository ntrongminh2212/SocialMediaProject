package com.example.postservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "tbl_post"
)
public class Post implements Serializable {
    @Id
    @SequenceGenerator(
            name = "post_sequence",
            sequenceName = "post_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_sequence"
    )
    private Long postID;
    private Long creatorID;
    private String statusContent;
    private String attachmentUrl;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "post_id",
            referencedColumnName = "postID"
    )
    private List<PostReaction> postReactions;
}












