package com.example.postservice.service;

import com.example.postservice.dto.ActivityDTO;
import com.example.postservice.dto.PostDTO;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<List<PostDTO>> getPostOfUser(Long userId);

//    Optional<PostDTO> createPost(PostDTO postDTO);

    Optional<PostDTO> createPost(PostDTO postDTO);

    Optional<PostDTO> getPostDetail(Long postId);

    List<PostDTO> getNewFeed(Long userId);

    boolean deletePost(PostDTO postDTO);

    List<ActivityDTO> getActivitiesHistory(Long userId);

    List<PostDTO> searchPosts(String searchStr);
}
