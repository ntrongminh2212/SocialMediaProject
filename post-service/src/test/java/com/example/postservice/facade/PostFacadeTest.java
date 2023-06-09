package com.example.postservice.facade;

import com.example.postservice.dto.PostDTO;
import com.example.postservice.entity.Post;
import com.example.postservice.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostFacadeTest {

    @Autowired
    private PostFacade postFacade;
    @MockBean
    private PostService postService;
    @BeforeEach
    void setUp() {
        Post post1 = Post.builder()
                .postId(1L)
                .creatorId(1l)
                .statusContent("Status 1")
                .build();
        Post post2 = Post.builder()
                .postId(2L)
                .creatorId(1l)
                .statusContent("Status 2")
                .build();
        Post post3 = Post.builder()
                .postId(3L)
                .creatorId(2l)
                .statusContent("Status 2")
                .build();

        List<Post> postsOfUserId1 = List.of(post1,post2);
        Mockito.when(postService.findByCreatorIdOrderByCreatedTimeDesc(1L))
                .thenReturn(postsOfUserId1);
    }

    //getPostOfUser
    @Test
    void whenUserIdIsValid_thenReturnPostsOfUserId() {
        PostDTO post1 = PostDTO.builder()
                .postId(1L)
                .userId(1l)
                .statusContent("Status 1")
                .build();
        PostDTO post2 = PostDTO.builder()
                .postId(2L)
                .userId(1l)
                .statusContent("Status 2")
                .build();
        List<PostDTO> expect = List.of(post1,post2);
        Long userId = 1L;
        List<PostDTO> actual = postFacade.getPostOfUser(userId);
        assertIterableEquals(expect,actual);
    }

    @Test
    void createPost() {
    }

    @Test
    void getPostDetail() {
    }

    @Test
    void getNewFeed() {
    }

    @Test
    void deletePost() {
    }

    @Test
    void getActivitiesHistory() {
    }

    @Test
    void searchPosts() {
    }

    @Test
    void getUserIdSet() {
    }
}