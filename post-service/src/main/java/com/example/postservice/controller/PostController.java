package com.example.postservice.controller;

import com.example.postservice.configuration.MessageConfig;
import com.example.postservice.dto.PostDTO;
import com.example.postservice.dto.PostReactionDTO;
import com.example.postservice.dto.ResponseDTO;
import com.example.postservice.service.PostReactionService;
import com.example.postservice.service.PostService;
import com.rabbitmq.tools.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/user")
    public ResponseEntity<Object> getPostsOfUser(
            @RequestParam("user_id") Long user_id,
            final HttpServletRequest request
    ){
        if (request.getHeader("userId") !=null) {
            return ResponseEntity.ok(postService.getPostOfUser(user_id).get());
        }
        return new ResponseEntity<>(ResponseDTO.BADREQUEST,HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/detail")
    public ResponseEntity<Object> getPostDetail(
            @RequestParam("post_id") Long post_id,
            final HttpServletRequest request
    ){
        if (request.getHeader("userId") !=null) {
            Optional<PostDTO> postDTO = postService.getPostDetail(post_id);
            if (postDTO.isPresent()){
                return ResponseEntity.ok(postDTO);
            }
        }
        return ResponseEntity.badRequest().body(ResponseDTO.BADREQUEST);
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createPost(
            @RequestBody PostDTO postDTO
    ) throws IOException {
        if (postDTO.getUserId()!=null) {
            rabbitTemplate.convertAndSend(MessageConfig.EXCHANGE, MessageConfig.ROUTING_KEY, postDTO);
            return ResponseEntity.ok(true);
        }
        return new ResponseEntity<>(ResponseDTO.BADREQUEST, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/react-post")
    public ResponseEntity<Object> reactToPost(
            @RequestBody PostReactionDTO postReactionDTO
    ){
        if (postReactionDTO.getUserId()!=null) {
            Optional<PostReactionDTO> reactionDTOPOtional = postReactionService.reactToPost(postReactionDTO);
            if (reactionDTOPOtional.isPresent()) {
                return ResponseEntity.ok(reactionDTOPOtional.get());
            }
        }
        return new ResponseEntity<>(ResponseDTO.BADREQUEST, HttpStatus.BAD_REQUEST);
    }

//    @DeleteMapping("/delete-post")
//    public
}






