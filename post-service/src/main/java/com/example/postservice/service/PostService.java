package com.example.postservice.service;

import com.example.postservice.dto.PostDTO;
import com.example.postservice.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
     PostDTO createPost(Post postDTO);
//    List<PostDTO> getPostOfUser(Long userId);
//
////    Optional<PostDTO> createPost(PostDTO postDTO);
//
//    Optional<PostDTO> createPost(PostDTO postDTO);
//
//    Optional<PostDTO> getPostDetail(Long postId);
//
//    List<PostDTO> getNewFeed(Long userId);
//
//    boolean deletePost(PostDTO postDTO);
//
//    List<ActivityDTO> getActivitiesHistory(Long userId);
//
//    List<PostDTO> searchPosts(String searchStr);

    List<Post> findByCreatorIdOrderByCreatedTimeDesc(Long userId);

    Optional<Post> findById(Long postId);

    List<Post> findByCreatorId(Long userId);

    List<Post> findBySearchString(String upperCase);

    Optional<Post> findByPostIdAndCreatorId(Long userId, Long postId);

    void deletePost(Post post);
}
