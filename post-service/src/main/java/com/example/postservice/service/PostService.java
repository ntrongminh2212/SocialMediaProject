package com.example.postservice.service;

import com.example.postservice.dto.PostDTO;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<List<PostDTO>> getPostOfUser(Long userId);

    Optional<PostDTO> createPost(PostDTO postDTO);
}
