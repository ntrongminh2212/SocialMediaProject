package com.example.friendservice.controller;

import com.example.friendservice.dto.FriendDTO;
import com.example.friendservice.dto.ResponseDTO;
import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.Friend;
import com.example.friendservice.service.FriendService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private final Map<String,Object> falseResponse;

    public FriendController() {
        this.falseResponse = new HashMap<>();
        this.falseResponse.put("error",400);
    }

    Logger logger = Logger.getLogger(FriendController.class);

    @GetMapping("/hellofriend")
    public String hello() {
        return "Hello";
    }

    @PostMapping("/request")
    public ResponseEntity<Object> requestFriend(
            @RequestBody Map<String, String> body
    ) {
        if (body.get("userId") !=null) {
            Optional<FriendDTO> friendDTO =friendService.requestFriend(body);
            if (friendDTO.isPresent()) {
                return ResponseEntity.ok(friendDTO.get());
            }
            ResponseDTO.BADREQUEST.put("message", "Friend request has already exist");
            return ResponseEntity.badRequest().body(ResponseDTO.BADREQUEST);
        }
        ResponseDTO.BADREQUEST.put("message", "User not identified");
        return ResponseEntity.badRequest().body(ResponseDTO.BADREQUEST);
    }

    @PutMapping("/request")
    public ResponseEntity<Object> acceptFriendRequest(
            @RequestBody Map<String, String> body
    ){
        if (body.get("userId") !=null) {
            int rs = friendService.acceptFriendRequest(body);
            if (rs > 0) {
                return ResponseEntity.ok(true);
            }
            return new ResponseEntity<>(ResponseDTO.NOTFOUND, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.badRequest().body(ResponseDTO.BADREQUEST);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getListFriend(
            @RequestHeader("userId") Long userId
    ){
        logger.info(userId);
        if (userId!=null){
            List<UserDTO> friendList = friendService.getFriendList(userId);
            return ResponseEntity.ok(friendList);
        }
        return ResponseEntity.badRequest().body(ResponseDTO.BADREQUEST);
    }
}









