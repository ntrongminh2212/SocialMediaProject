package com.example.postservice.feignclient;

import com.example.postservice.dto.PostReactionDTO;
import com.example.postservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "USER-SERVICE", path = "/user/")
public interface UserClient {
    @GetMapping("/info/{user_id}")
    Optional<UserDTO> getUserInfo(
            @RequestHeader(value = "Authorization", required = true) String authHeader,
            @PathVariable("user_id") Long user_id
    );

    @PostMapping("/info/list")
    List<PostReactionDTO> getUserReactionDetail(
            @RequestHeader(value = "Authorization", required = true) String authHeader,
            @RequestBody List<PostReactionDTO> lstPostReactionDTO
    );
}
