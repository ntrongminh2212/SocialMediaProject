package com.example.postservice.feignclient;

import com.example.postservice.dto.PostReactionDTO;
import com.example.postservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
    @GetMapping("/user/info/{user_id}")
    Optional<UserDTO> getUserInfo(
            @PathVariable("user_id") Long user_id
    );

    @GetMapping("/friend/list")
    ResponseEntity<List<UserDTO>> getListFriend(
            @RequestHeader("userId") Long userId
    );

    @PostMapping("/user/list-user-details")
    Map<Long,UserDTO> getListUserDetail(@RequestBody Set<Long> lstUserId);
}
