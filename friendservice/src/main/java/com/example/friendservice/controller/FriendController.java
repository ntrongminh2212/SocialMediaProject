package com.example.friendservice.controller;

import com.example.friendservice.dto.FriendDTO;
import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.facade.FriendFacade;

import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/friend")
public class FriendController {
    @Autowired
    private FriendFacade friendFacade;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    Logger logger = Logger.getLogger(FriendController.class);

    @PostMapping("/request")
    public ResponseEntity<FriendDTO> requestFriend(@RequestHeader Long userId, @RequestBody Map<String, String> body) {
        Optional<FriendDTO> friendDTO = friendFacade.requestFriend(body);
        if (friendDTO.isPresent()) {
            return ResponseEntity.ok(friendDTO.get());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Friend request has already exist");
    }

    @PutMapping("/request")
    public ResponseEntity<Boolean> acceptFriendRequest(
            @RequestHeader Long userId, @RequestBody Map<String, String> body) {
        int rs = friendFacade.acceptFriendRequest(body);
        if (rs > 0) {
            return ResponseEntity.ok(true);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Friend request not exist");
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserDTO>> getListFriend(@RequestHeader("userId") Long userId) {
        List<UserDTO> friendList = friendFacade.getFriendList(userId);
        return ResponseEntity.ok(friendList);
    }
}
