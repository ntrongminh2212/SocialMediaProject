package com.example.postservice.controller;

import com.example.postservice.configuration.MessageConfig;
import com.example.postservice.dto.PostDTO;
import com.example.postservice.dto.PostReactionDTO;
import com.example.postservice.service.PostReactionService;
import com.example.postservice.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/post")
public class PostController {
    private static Logger logger = Logger.getLogger(PostController.class);
    @Autowired
    private PostService postService;
    @Autowired
    private PostReactionService postReactionService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/{user_id}")
    public Optional<List<PostDTO>> getPostsOfUser(
            @PathVariable("user_id") Long user_id,
            final HttpServletRequest request
    ){
        logger.info("request come "+request.getHeader("userId"));
        return postService.getPostOfUser(user_id);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(
            @RequestBody PostDTO postDTO
    ) throws IOException {
         rabbitTemplate.convertAndSend(MessageConfig.EXCHANGE,MessageConfig.ROUTING_KEY,postDTO);
         return ResponseEntity.ok(true);
    }

    @PostMapping("/react-post")
    public Optional<PostReactionDTO> reactToPost(
            @RequestBody PostReactionDTO postReactionDTO
    ){
        return postReactionService.reactToPost(postReactionDTO);
    }
}






