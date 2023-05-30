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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public FriendDTO requestFriend(
            @RequestBody Map<String, String> body
    ) {
        logger.info("[Friend request come]");
        return friendService.requestFriend(body);
    }

    @PutMapping("/request")
    public ResponseEntity<Object> acceptFriendRequest(
            @RequestBody Map<String, String> body
    ){

        int rs= friendService.acceptFriendRequest(body);
        if (rs>0){
            return ResponseEntity.ok(true);
        }
        return new ResponseEntity<Object>(falseResponse, HttpStatus.NOT_FOUND);
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









