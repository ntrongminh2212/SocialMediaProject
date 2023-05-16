package com.example.postservice.service;

import com.example.postservice.controller.PostController;
import com.example.postservice.dto.PostDTO;
import com.example.postservice.dto.PostReactionDTO;
import com.example.postservice.dto.UserDTO;
import com.example.postservice.entity.Post;
import com.example.postservice.entity.PostReaction;
import com.example.postservice.feignclient.UserClient;
import com.example.postservice.mapper.PostMapper;
import com.example.postservice.mapper.PostMapperImpl;
import com.example.postservice.mapper.PostReactionMapper;
import com.example.postservice.repository.PostReactionRepository;
import com.example.postservice.repository.PostRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private Logger logger = Logger.getLogger(PostServiceImpl.class);
    @Autowired
    UserClient userClient;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostReactionRepository reactionRepository;
    @Autowired
    PostMapper postMapper;
    @Autowired
    PostReactionMapper postReactionMapper;

    @Override
    public Optional<List<PostDTO>> getPostOfUser(Long userId, String authHeader) {
        Optional<UserDTO> userDTO = userClient.getUserInfo(authHeader, userId);
        if (userDTO.isPresent()) {
            Optional<List<Post>> lstPostByUserId = postRepository.findByUserId(userId);

            for (Post post : lstPostByUserId.get()) {
                Optional<List<PostReaction>> lstPostReaction = reactionRepository.findByPostId(post.getPostId());
                List<PostReactionDTO> lstPostReactionDTO = postReactionMapper.getLstPostReactionDTO(lstPostReaction.get());
            }

        }
        return Optional.empty();
    }
}







