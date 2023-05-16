package com.example.postservice.id;

import com.example.postservice.entity.Post;
import jakarta.persistence.Embeddable;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PostReactionId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    private Long userId;
}
