package com.example.postservice.controller;

import com.example.postservice.entity.Post;
import com.example.postservice.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/post")
public class PostController {

    private Logger logger = Logger.getLogger(PostController.class);
    @Autowired
    PostService postService;
    @GetMapping("/personal/{user_id}")
    public Optional<List<PostDTO>> getPostsOfUser(
            @PathVariable("user_id") Long user_id,
            final HttpServletRequest request
    ){
        String authHeader = request.getHeader("Authorization");
        logger.info("[Authorization header] "+ authHeader);
        return postService.getPostOfUser(user_id,authHeader);
    }
}






