package com.example.postservice.controller;

import com.example.postservice.dto.ActivityDTO;
import com.example.postservice.dto.PostDTO;
import com.example.postservice.dto.PostReactionDTO;
import com.example.postservice.dto.ResponseDTO;
import com.example.postservice.service.PostReactionService;
import com.example.postservice.service.PostService;
import com.example.postservice.service.StatisticService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/post")
public class PostController {
    private static Logger logger = Logger.getLogger(PostController.class);
    @Autowired
    private PostService postService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private PostReactionService postReactionService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/user")
    public ResponseEntity<Object> getPostsOfUser(
            @RequestHeader Long userId,
            @RequestParam("user_id") Long user_id,
            final HttpServletRequest request
    ) {
        return ResponseEntity.ok(postService.getPostOfUser(user_id).get());
    }

    @GetMapping("/detail")
    public ResponseEntity<Object> getPostDetail(
            @RequestParam("post_id") Long post_id,
            @RequestHeader Long userId,
            final HttpServletRequest request
    ) {
        Optional<PostDTO> postDTO = postService.getPostDetail(post_id);
        if (postDTO.isPresent()) {
            return ResponseEntity.ok(postDTO);
        }
        return new ResponseEntity<>(ResponseDTO.NOTFOUND, HttpStatus.NOT_FOUND);
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createPost(
            @RequestHeader Long userId,
            @RequestBody PostDTO postDTO
    ) throws IOException {
        postDTO = postService.createPost(postDTO).get();
        return ResponseEntity.ok(postDTO);
    }

    @PostMapping("/react-post")
    public ResponseEntity<Object> reactToPost(
            @RequestHeader Long userId,
            @RequestBody PostReactionDTO postReactionDTO
    ) {
        Optional<PostReactionDTO> reactionDTOPOtional = postReactionService.reactToPost(postReactionDTO);
        if (reactionDTOPOtional.isPresent()) {
            return ResponseEntity.ok(reactionDTOPOtional.get());
        }
        return new ResponseEntity<>(ResponseDTO.NOTFOUND, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete-post")
    public ResponseEntity<Object> deletePost(
            @RequestHeader Long userId,
            @RequestBody PostDTO postDTO
    ) {
        boolean rs = postService.deletePost(postDTO);
        if (rs) return ResponseEntity.ok(new HashMap<>() {{
            put("status", 200);
            put("success", true);
        }});
        else return new ResponseEntity<>(ResponseDTO.NOTFOUND, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/new-feeds")
    public ResponseEntity<List<PostDTO>> getNewFeeds(
            @RequestHeader Long userId
    ) {
        List<PostDTO> newFeedPostList = postService.getNewFeed(userId);
        return ResponseEntity.ok(newFeedPostList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> searchPosts(
            @RequestParam String searchStr,
            @RequestHeader Long userId
    ){
        List<PostDTO> postDTOList = postService.searchPosts(searchStr);
        return ResponseEntity.ok(postDTOList);
    }

    @GetMapping("/activities-history")
    public ResponseEntity<List<ActivityDTO>> getActivitiesHistory(
            @RequestHeader Long userId
    ){
        List<ActivityDTO> activityDTOS = postService.getActivitiesHistory(userId);
        return ResponseEntity.ok(activityDTOS);
    }

    @GetMapping("/day-excel")
    public void exportPostsByDayToExcel(
            @RequestHeader Long userId,
            @RequestParam String dayFrom,
            @RequestParam String dayTo,
            HttpServletResponse response
    ) throws IOException, ParseException {
        statisticService.exportPostsByDayBetweenToExcel(dayFrom,dayTo,response);
    }

    @GetMapping("/post-to-csv-job")
    public void exportPostsToCsv(
            @RequestHeader Long userId
    ){
        statisticService.exportPostsToCsv();
    }
}






