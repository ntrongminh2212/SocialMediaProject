package com.example.postservice.controller;

import com.example.postservice.dto.*;
import com.example.postservice.facade.PostFacade;
import com.example.postservice.service.PostReactionService;
import com.example.postservice.service.PostService;
import com.example.postservice.service.StatisticService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/post")
public class PostController {
    @Autowired
    private PostFacade postFacade;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private PostReactionService postReactionService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/user")
    public ResponseEntity<List<PostDTO>> getPostsOfUser(@RequestHeader Long userId, @RequestParam("id") Long id, final HttpServletRequest request) {
        return ResponseEntity.ok(postFacade.getPostOfUser(id));
    }

    @GetMapping("/detail")
    public ResponseEntity<PostDTO> getPostDetail(@RequestParam("post_id") Long post_id, @RequestHeader Long userId, final HttpServletRequest request) {
        Optional<PostDTO> postDTO = postFacade.getPostDetail(post_id);
        if (postDTO.isPresent()) {
            return ResponseEntity.ok(postDTO.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/create")
    public ResponseEntity<PostDTO> createPost(@RequestHeader Long userId, @RequestBody PostDTO postDTO) throws IOException {
        postDTO = postFacade.createPost(postDTO);
        return ResponseEntity.ok(postDTO);
    }

    @PostMapping("/react-post")
    public ResponseEntity<Object> reactToPost(@RequestHeader Long userId, @RequestBody PostReactionDTO postReactionDTO) {
        Optional<PostReactionDTO> reactionDTOPOtional = postReactionService.reactToPost(postReactionDTO);
        if (reactionDTOPOtional.isPresent()) {
            return ResponseEntity.ok(reactionDTOPOtional.get());
        }
        return new ResponseEntity<>(ResponseDTO.NOTFOUND, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete-post")
    public ResponseEntity<Object> deletePost(@RequestHeader Long userId, @RequestBody PostDTO postDTO) {
        boolean rs = postFacade.deletePost(postDTO);
        if (rs) return ResponseEntity.ok(ResponseDTO.SUCCESS);
        else return new ResponseEntity<>(ResponseDTO.NOTFOUND, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/new-feeds")
    public ResponseEntity<List<PostDTO>> getNewFeeds(@RequestHeader Long userId) {
        List<PostDTO> newFeedPostList = postFacade.getNewFeed(userId);
        return ResponseEntity.ok(newFeedPostList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> searchPosts(@RequestParam String searchStr, @RequestHeader Long userId) {
        List<PostDTO> postDTOList = postFacade.searchPosts(searchStr);
        return ResponseEntity.ok(postDTOList);
    }

    @GetMapping("/activities-history")
    public ResponseEntity<List<ActivityDTO>> getActivitiesHistory(@RequestHeader Long userId) {
        List<ActivityDTO> activityDTOS = postFacade.getActivitiesHistory(userId);
        return ResponseEntity.ok(activityDTOS);
    }

    @GetMapping("/day-excel")
    public void exportPostsByDayToExcel(@RequestHeader Long userId, @RequestHeader String role, @RequestParam String dayFrom, @RequestParam String dayTo, HttpServletResponse response) throws IOException, ParseException {
        if (role.compareTo(Role.ADMIN.name()) == 0) {
            statisticService.exportPostsByDayBetweenToExcel(dayFrom, dayTo, response);
        }
        throw new ForbiddenException();
    }

    @GetMapping("/post-to-csv-job")
    public void exportPostsToCsv(@RequestHeader Long userId, @RequestHeader String role) {
        if (role.compareTo(Role.ADMIN.name()) == 0) {
            statisticService.exportPostsToCsv();
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
}






