package com.example.cloudgateway.feignclient;

import com.example.cloudgateway.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE", path = "/user/",url = "http://localhost:9001")
public interface UserClient {
    @GetMapping("/authenticate")
    public ResponseEntity<UserDTO> authenticate(@RequestHeader(value = HttpHeaders.AUTHORIZATION,required = true) String authToken);

}
