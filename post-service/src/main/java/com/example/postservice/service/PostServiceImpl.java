package com.example.postservice.service;

import com.cloudinary.Cloudinary;
import com.example.postservice.dto.PostDTO;
import com.example.postservice.dto.PostReactionDTO;
import com.example.postservice.entity.Post;
import com.example.postservice.entity.PostReaction;
import com.example.postservice.feignclient.UserClient;
import com.example.postservice.mapper.PostMapper;
import com.example.postservice.mapper.PostReactionMapper;
import com.example.postservice.repository.PostReactionRepository;
import com.example.postservice.repository.PostRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

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
    @Autowired
    CloudinaryService cloudinaryService;

    @Override
    public Optional<List<PostDTO>> getPostOfUser(Long userId) {
            Optional<List<Post>> lstPostByUserId = postRepository.findByCreatorId(userId);
            logger.info("[Got all user posts]");
            List<PostDTO> lstPostDTO = new ArrayList<>();
            for (Post post : lstPostByUserId.get()) {
                Optional<List<PostReaction>> lstPostReaction = reactionRepository.findByPost(post.getPostId());
                List<PostReactionDTO> lstPostReactionDTO = postReactionMapper.getLstPostReactionDTO(lstPostReaction.get());
                logger.info("[Request reaction for post "+post.getPostId()+"]");
                lstPostReactionDTO = userClient.getUserReactionDetail(lstPostReactionDTO);
                PostDTO postDTO = postMapper.postToDTO(post,lstPostReactionDTO);
                lstPostDTO.add(postDTO);
            }
            logger.info("[Done]");
        return Optional.of(lstPostDTO);
    }

    @Override
    public Optional<PostDTO> createPost(PostDTO postDTO) {
        Map result = cloudinaryService.uploadImage(postDTO.getAttachmentUrl());
        postDTO.setAttachmentUrl(String.valueOf(result.get("url")));
        Post post = postMapper.postToEntity(postDTO);
        return Optional.ofNullable(postMapper.postToDTO(postRepository.save(post)));
    }

}







