package com.example.postservice.service;

import com.example.postservice.dto.PostReactionDTO;

import java.util.Optional;

public interface PostReactionService {
    Optional<PostReactionDTO> reactToPost(PostReactionDTO postReactionDTO);
}
