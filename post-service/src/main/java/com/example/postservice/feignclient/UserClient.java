package com.example.postservice.feignclient;

import com.example.postservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
    @GetMapping("/info/{user_id}")
    public Optional<UserDTO> getUserInfo(
            @RequestHeader(value = "Authorization", required = true) String authHeader,
            @PathVariable("user_id") Long user_id
    );
}
