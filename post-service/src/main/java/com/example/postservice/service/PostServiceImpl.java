package com.example.postservice.service;

import com.example.postservice.dto.*;
import com.example.postservice.entity.Comment;
import com.example.postservice.entity.CommentReaction;
import com.example.postservice.entity.Post;
import com.example.postservice.entity.PostReaction;
import com.example.postservice.feignclient.UserClient;
import com.example.postservice.mapper.CommentMapper;
import com.example.postservice.mapper.CommentReactionMapper;
import com.example.postservice.mapper.PostMapper;
import com.example.postservice.mapper.PostReactionMapper;
import com.example.postservice.repository.CommentReactionRepository;
import com.example.postservice.repository.CommentRepository;
import com.example.postservice.repository.PostReactionRepository;
import com.example.postservice.repository.PostRepository;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    CloudinaryService cloudinaryService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Optional<List<PostDTO>> getPostOfUser(Long userId) {
        Optional<List<Post>> lstPostByUserId = postRepository.findByCreatorIdOrderByCreatedTimeDesc(userId);
        logger.info("[Got all user posts]");
        List<PostDTO> lstPostDTO = new ArrayList<>();
        for (Post post : lstPostByUserId.get()) {
            Optional<List<PostReaction>> lstPostReaction = reactionRepository.findByPostId(post.getPostId());
            List<PostReactionDTO> lstPostReactionDTO = postReactionMapper.postReactionListToDTO(lstPostReaction.get());
            logger.info("[Request reaction for post " + post.getPostId() + "]");
            lstPostReactionDTO = userClient.getUserReactionDetail(lstPostReactionDTO);
            PostDTO postDTO = postMapper.postToDTO(post, lstPostReactionDTO);
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

//    @RabbitListener(queues = MessageConfig.QUEUE)
//    public void createPost(PostDTO postDTO) {
//        logger.info("[Got new post from queue, creating post]");
//        Map result = cloudinaryService.uploadImage(postDTO.getAttachmentUrl());
//        postDTO.setAttachmentUrl(String.valueOf(result.get("url")));
//        Post post = postMapper.postToEntity(postDTO);
//        postRepository.save(post);
//    }

    @Override
    public Optional<PostDTO> getPostDetail(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            PostDTO postDTO = postMapper.postToDTO(post.get());
            List<PostReaction> postReactions = new ArrayList<>();
            List<Comment> comments = new ArrayList<>();

            postReactions = reactionRepository.findByPostId(postId).get();
            comments = commentRepository.findByPostId(postId);

            List<CommentDTO> commentDTOList = commentMapper.commentListToDTO(comments);
            for (CommentDTO commentDTO :
                    commentDTOList) {
                List<CommentReaction> commentReactionList =
                        commentReactionRepository.findByCommentId(commentDTO.getCommentId());
                List<CommentReactionDTO> commentReactionDTOList =
                        commentReactionMapper.commentReactionListToDTO(commentReactionList);
                commentDTO.setCommentReactionDTOList(commentReactionDTOList);
            }
            List<PostReactionDTO> postReactionDTOList = postReactionMapper.postReactionListToDTO(postReactions);
            postDTO.setComments(commentDTOList);
            postDTO.setPostReactions(postReactionDTOList);
            return Optional.of(postDTO);
        }
        return Optional.empty();
    }

    @Override
    public List<PostDTO> getNewFeed(Long userId) {
        //Get friends list and group list
        List<UserDTO> userDTOList = userClient.getListFriend(userId).getBody();
        //Get post of each friend and group
        List<PostDTO> newFeeds = new ArrayList<>();
        for (UserDTO userDTO :
                userDTOList) {
            List<PostDTO> postDTOList = getPostOfUser(userDTO.getUserId()).get();
            newFeeds.addAll(postDTOList);
        }
        //Arrange by date create DESC
        Collections.sort(newFeeds, (postDTO1, postDTO2) -> {
            return Math.toIntExact(postDTO2.getCreatedTime().getTime() - postDTO1.getCreatedTime().getTime());
        });
        return newFeeds;
    }

    @Override
    public boolean deletePost(PostDTO postDTO) {
        Optional<Post> postOptional = postRepository.findByPostIdAndCreatorId(postDTO.getPostId(), postDTO.getUserId());
        if (postOptional.isPresent()) {
            Map<String,String> rs = cloudinaryService.deleteImage(postOptional.get().getAttachmentUrl());
            if (rs!=null) {
                Optional<List<PostReaction>> postReactions = reactionRepository.findByPostId(postOptional.get().getPostId());
                List<Comment> comments = commentRepository.findByPostId(postOptional.get().getPostId());

                reactionRepository.deleteAll(postReactions.get());
                for (Comment comment:
                     comments) {
                    commentReactionRepository.deleteAll(
                            commentReactionRepository.findByCommentId(comment.getCommentId())
                    );
                }
                commentRepository.deleteAll(comments);
                postRepository.delete(postOptional.get());
            }
            return true;
        }
        return false;
    }
}







