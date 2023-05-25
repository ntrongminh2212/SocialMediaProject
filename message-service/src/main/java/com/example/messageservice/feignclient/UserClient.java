package com.example.messageservice.feignclient;

import com.example.messageservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "USER-SERVICE", path = "/user/")
public interface UserClient {
    @GetMapping("/info/{user_id}")
    Optional<UserDTO> getUserInfo(
            @PathVariable("user_id") Long user_id
    );
}
