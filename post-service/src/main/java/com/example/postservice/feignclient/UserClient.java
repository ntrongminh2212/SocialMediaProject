package com.example.postservice.feignclient;

import com.example.postservice.dto.PostReactionDTO;
import com.example.postservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
    @GetMapping("/user/info/{user_id}")
    Optional<UserDTO> getUserInfo(
            @PathVariable("user_id") Long user_id
    );

    @PostMapping("/user/reaction-details")
    List<PostReactionDTO> getUserReactionDetail(
            @RequestBody List<PostReactionDTO> lstPostReactionDTO
    );

    @GetMapping("/friend/list")
    ResponseEntity<List<UserDTO>> getListFriend(
            @RequestHeader("userId") Long userId
    );
}
