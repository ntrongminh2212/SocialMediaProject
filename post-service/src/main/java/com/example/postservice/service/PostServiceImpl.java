package com.example.postservice.service;

import com.example.postservice.dto.*;
import com.example.postservice.entity.Comment;
import com.example.postservice.entity.CommentReaction;
import com.example.postservice.entity.Post;
import com.example.postservice.entity.PostReaction;
import com.example.postservice.feignclient.UserClient;
import com.example.postservice.mapper.*;
import com.example.postservice.repository.CommentReactionRepository;
import com.example.postservice.repository.CommentRepository;
import com.example.postservice.repository.PostReactionRepository;
import com.example.postservice.repository.PostRepository;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    ActivityMapper activityMapper;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    public Optional<List<PostDTO>> getPostOfUser(Long userId) {
        Optional<List<Post>> lstPostByUserId = postRepository.findByCreatorIdOrderByCreatedTimeDesc(userId);
        Map<Long, UserDTO> userDTOMap = userClient.getListUserDetail(
                getUserIdSet(lstPostByUserId.get())
        );
        List<PostDTO> lstPostDTO = new ArrayList<>();
        for (Post post : lstPostByUserId.get()) {
            Optional<List<PostReaction>> lstPostReaction = reactionRepository.findByPostId(post.getPostId());
            List<PostReactionDTO> lstPostReactionDTO = postReactionMapper.postReactionListToDTO(lstPostReaction.get());
            logger.info("[Request reaction for post " + post.getPostId() + "]");
            for (PostReactionDTO postReactionDTO : lstPostReactionDTO) {
                postReactionDTO.setUser(userDTOMap.get(postReactionDTO.getUserId()));
            }
            PostDTO postDTO = postMapper.postToDTO(post, lstPostReactionDTO);
            postDTO.setUser(userDTOMap.get(postDTO.getUserId()));
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
            Map<Long, UserDTO> userDTOMap = userClient.getListUserDetail(
                    getUserIdSet(new ArrayList<>() {{
                        add(post.get());
                    }})
            );
            PostDTO postDTO = postMapper.postToDTO(post.get());
            List<PostReactionDTO> postReactionDTOList = postReactionMapper.postReactionListToDTO(
                    post.get().getPostReactions()
            );
            for (PostReactionDTO postReactionDTO : postReactionDTOList) {
                postReactionDTO.setUser(userDTOMap.get(postReactionDTO.getUserId()));
            }

            List<CommentDTO> commentDTOList = new ArrayList<>();
            for (Comment comment :
                    post.get().getComments()) {
                List<CommentReactionDTO> commentReactionDTOList =
                        commentReactionMapper.commentReactionListToDTO(
                                comment.getCommentReactions()
                        );
                for (CommentReactionDTO commentReactionDTO : commentReactionDTOList) {
                    commentReactionDTO.setUser(userDTOMap.get(commentReactionDTO.getUserId()));
                }
                CommentDTO commentDTO = commentMapper.commentToDTO(comment);
                commentDTO.setCommentReactionDTOList(commentReactionDTOList);
                commentDTO.setUser(userDTOMap.get(commentDTO.getUserId()));
                commentDTOList.add(commentDTO);
            }

            postDTO.setComments(commentDTOList);
            postDTO.setUser(userDTOMap.get(postDTO.getUserId()));
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
        for (UserDTO userDTO : userDTOList) {
            List<PostDTO> postDTOList = getPostOfUser(userDTO.getUserId()).get();
            newFeeds.addAll(postDTOList);
        }
        //Arrange by date create DESC
        Collections.sort(newFeeds, (postDTO1, postDTO2) -> {
            try {
                Date createTimed01 = dateTimeFormat.parse(postDTO1.getCreatedTime());
                Date createTimed02 = dateTimeFormat.parse(postDTO2.getCreatedTime());
                return Math.toIntExact(createTimed02.getTime() - createTimed01.getTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        return newFeeds;
    }

    @Override
    public boolean deletePost(PostDTO postDTO) {
        Optional<Post> postOptional = postRepository.findByPostIdAndCreatorId(postDTO.getPostId(), postDTO.getUserId());
        if (postOptional.isPresent()) {
            Map<String, String> rs = cloudinaryService.deleteImage(postOptional.get().getAttachmentUrl());
            if (rs != null) {
                Optional<List<PostReaction>> postReactions = reactionRepository.findByPostId(postOptional.get().getPostId());
                List<Comment> comments = commentRepository.findByPostId(postOptional.get().getPostId());

                reactionRepository.deleteAll(postReactions.get());
                for (Comment comment :
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

    @Override
    public List<ActivityDTO> getActivitiesHistory(Long userId) {
        //Post
        List<ActivityDTO> activityDTOS = new ArrayList<>();
        activityDTOS.addAll(activityMapper.postToActivityDTO(
                postMapper.postToDTO(
                        postRepository.findByCreatorId(userId)
                )
        ));
        //PostReaction
        activityDTOS.addAll(activityMapper.postReactionToActivityDTO(
                postReactionMapper.postReactionListToDTO(
                        reactionRepository.findByPostReactionIdUserId(userId)
                )
        ));
        //Comment
        activityDTOS.addAll(activityMapper.commentToActivityDTO(
                commentMapper.commentListToDTO(
                        commentRepository.findByUserId(userId)
                )
        ));
        //CommentReaction
        activityDTOS.addAll(activityMapper.commentReactionToActivityDTO(
                commentReactionMapper.commentReactionListToDTO(
                        commentReactionRepository.findByCommentReactionIdUserId(userId)
                )
        ));

        Collections.sort(activityDTOS, (activity01, activity02) -> {
            try {
                Date actionTime01 = dateTimeFormat.parse(activity01.getActionTime());
                Date actionTime02 = dateTimeFormat.parse(activity02.getActionTime());
                return Math.toIntExact(actionTime02.getTime() - actionTime01.getTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        return activityDTOS;
    }

    @Override
    public List<PostDTO> searchPosts(String searchStr) {
        List<PostDTO> postDTOList = postMapper.postToDTO(
                postRepository.findBySearchString(searchStr.toUpperCase())
        );
        return postDTOList;
    }

    Set<Long> getUserIdSet(List<Post> lstPost) {
        Set<Long> listUserId = new HashSet<>();
        for (Post post : lstPost) {
            listUserId.add(post.getCreatorId());
            for (PostReaction postReaction : post.getPostReactions()) {
                listUserId.add(postReaction.getPostReactionId().getUserId());
            }

            for (Comment comment : post.getComments()) {
                listUserId.add(comment.getUserId());
                for (CommentReaction commentReaction : comment.getCommentReactions()) {
                    listUserId.add(commentReaction.getCommentReactionId().getUserId());
                }
            }
        }
        return listUserId;
    }
}







