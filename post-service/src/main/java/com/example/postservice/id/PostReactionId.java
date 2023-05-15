package com.example.postservice.id;

import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostReactionId implements Serializable {
    private Long postReactionId;
    private Long userId;
}
