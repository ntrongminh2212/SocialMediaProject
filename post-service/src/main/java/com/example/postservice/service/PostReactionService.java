package com.example.postservice.service;

import com.example.postservice.dto.PostReactionDTO;
import com.example.postservice.entity.PostReaction;

import java.util.List;
import java.util.Optional;

public interface PostReactionService {
    Optional<PostReactionDTO> reactToPost(PostReactionDTO postReactionDTO);

    List<PostReactionDTO>findByPostId(Long postId);

    List<PostReaction> findByPostReactionIdUserId(Long userId);
}
