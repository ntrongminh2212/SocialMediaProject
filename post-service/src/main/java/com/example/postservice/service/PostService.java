package com.example.postservice.service;

import com.example.postservice.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<List<Post>> getPostOfUser(Long userId, String authHeader);
}
