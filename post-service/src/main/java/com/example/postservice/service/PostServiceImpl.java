package com.example.postservice.service;

import com.example.postservice.dto.*;
import com.example.postservice.entity.Post;
import com.example.postservice.feignclient.UserClient;
import com.example.postservice.mapper.*;
import com.example.postservice.repository.CommentReactionRepository;
import com.example.postservice.repository.CommentRepository;
import com.example.postservice.repository.PostReactionRepository;
import com.example.postservice.repository.PostRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {
    private final Logger logger = Logger.getLogger(PostServiceImpl.class);
    @Autowired
    UserClient userClient;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostReactionRepository reactionRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentReactionRepository commentReactionRepository;
    @Autowired
    PostMapper postMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    PostReactionMapper postReactionMapper;
    @Autowired
    CommentReactionMapper commentReactionMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    CloudinaryService cloudinaryService;
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    public PostDTO createPost(Post post) {
        return postMapper.postToDTO(postRepository.save(post),false);
    }

    @Override
    public List<Post> findByCreatorIdOrderByCreatedTimeDesc(Long userId) {
        Optional<List<Post>>optionalPostList = postRepository.findByCreatorIdOrderByCreatedTimeDesc(userId);
        return optionalPostList.orElseGet(ArrayList::new);
    }

    @Override
    public Optional<Post> findById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post;
    }

    @Override
    public List<Post> findByCreatorId(Long userId) {
        return postRepository.findByCreatorId(userId);
    }

    @Override
    public List<Post> findBySearchString(String upperCase) {
        return postRepository.findBySearchString(upperCase);
    }
}







