package com.example.cloudgateway.feignclient;

import com.example.cloudgateway.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@FeignClient(name = "USER-SERVICE", path = "/user/")
public interface UserClient {
    @GetMapping("/info/{user_id}")
    Optional<UserDTO> getUserInfo(
            @PathVariable("user_id") Long user_id
    );
}
