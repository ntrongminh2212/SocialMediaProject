package com.example.postservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
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
    @Column(name = "post_id")
    @SequenceGenerator(
            name = "post_sequence",
            sequenceName = "post_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_sequence"
    )
    private Long postId;
    @Column(name = "creator_id")
    private Long creatorId;
    private String statusContent;
    private String attachmentUrl;
    @Column(name = "created_time")
    @CreationTimestamp
    private Date createdTime;
    @Column(name = "updated_datetime")
    @UpdateTimestamp
    private Date updatedTime;
}












