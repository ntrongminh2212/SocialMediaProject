package com.example.postservice.controller;

import com.example.postservice.dto.PostDTO;
import com.example.postservice.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/post")
public class PostController {
    private static Logger logger = Logger.getLogger(PostController.class);
    @Autowired
    private PostService postService;
    @GetMapping("/{user_id}")
    public Optional<List<PostDTO>> getPostsOfUser(
            @PathVariable("user_id") Long user_id,
            final HttpServletRequest request
    ){
        return postService.getPostOfUser(user_id);
    }

    @PostMapping("/create")
    public Optional<PostDTO> createPost(
            @RequestBody PostDTO postDTO
    ){
        return postService.createPost(postDTO);
    }
}






