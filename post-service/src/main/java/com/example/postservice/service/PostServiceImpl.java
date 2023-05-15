package com.example.postservice.service;

import com.example.postservice.controller.PostController;
import com.example.postservice.dto.UserDTO;
import com.example.postservice.entity.Post;
import com.example.postservice.feignclient.UserClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService{
    private Logger logger = Logger.getLogger(PostServiceImpl.class);
    @Autowired
    UserClient userClient;
    @Override
    public Optional<List<Post>> getPostOfUser(Long userId, String authHeader) {
        Optional<UserDTO> userDTO = userClient.getUserInfo(authHeader,userId);
        logger.info("[Response User]: "+userDTO.get().getEmail()+userDTO.get().getPhoneNum());
        return Optional.empty();
    }
}
