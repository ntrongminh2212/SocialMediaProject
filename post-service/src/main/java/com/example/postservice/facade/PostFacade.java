package com.example.postservice.facade;

import com.example.postservice.dto.*;
import com.example.postservice.entity.Comment;
import com.example.postservice.entity.CommentReaction;
import com.example.postservice.entity.Post;
import com.example.postservice.entity.PostReaction;
import com.example.postservice.feignclient.UserClient;
import com.example.postservice.mapper.*;
import com.example.postservice.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PostFacade {
    private final Logger logger = Logger.getLogger(PostServiceImpl.class);
    @Autowired
    UserClient userClient;
    @Autowired
    PostService postService;
    @Autowired
    PostReactionService postReactionService;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentReactionService commentReactionService;
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

    public List<PostDTO> getPostOfUser(Long userId) {
        List<Post> listPost = postService.findByCreatorIdOrderByCreatedTimeDesc(userId);
        if (listPost.isEmpty()) return new ArrayList<>();
        Map<Long, UserDTO> userDTOMap = userClient.getListUserDetail(
                getUserIdSet(listPost)
        );
        List<PostDTO> listPostDTO = new ArrayList<>();
        for (Post post : listPost) {
            PostDTO postDTO = postMapper.postToDTO(post,false);
            postDTO.setUser(userDTOMap.get(postDTO.getUserId()));
            listPostDTO.add(postDTO);
        }
        logger.info("[Done]");
        return listPostDTO;
    }

    public PostDTO createPost(PostDTO postDTO) {
        Map<String, String> result = cloudinaryService.uploadImage(postDTO.getAttachmentUrl());
        postDTO.setAttachmentUrl(String.valueOf(result.get("url")));
        Post post = postMapper.postToEntity(postDTO);
        return postService.createPost(post);
    }

//    @RabbitListener(queues = MessageConfig.QUEUE)
//    public void createPost(PostDTO postDTO) {
//        logger.info("[Got new post from queue, creating post]");
//        Map result = cloudinaryService.uploadImage(postDTO.getAttachmentUrl());
//        postDTO.setAttachmentUrl(String.valueOf(result.get("url")));
//        Post post = postMapper.postToEntity(postDTO);
//        postRepository.save(post);
//    }

    public Optional<PostDTO> getPostDetail(Long postId) {
        Optional<Post> post = postService.findById(postId);
        if (post.isPresent()) {
            ArrayList<Post> listPost = new ArrayList<>();
            listPost.add(post.get());
            Map<Long, UserDTO> userDTOMap = userClient.getListUserDetail(
                    getUserIdSet(listPost)
            );

            PostDTO postDTO = postMapper.postToDTO(post.get(),true);
            for (PostReactionDTO postReactionDTO : postDTO.getPostReactions()) {
                postReactionDTO.setUser(userDTOMap.get(postReactionDTO.getUserId()));
            }

            for (CommentDTO commentDTO : postDTO.getComments()) {
                for (CommentReactionDTO commentReactionDTO : commentDTO.getCommentReactionDTOList()) {
                    commentReactionDTO.setUser(userDTOMap.get(commentReactionDTO.getUserId()));
                }
                commentDTO.setUser(userDTOMap.get(commentDTO.getUserId()));
            }
            postDTO.setUser(userDTOMap.get(post.get().getCreatorId()));
            return Optional.of(postDTO);
        }
        return Optional.empty();
    }

    public List<PostDTO> getNewFeed(Long userId) {
        //Get friends list and group list
        List<UserDTO> userDTOList = userClient.getListFriend(userId).getBody();
        //Get post of each friend and group
        List<PostDTO> newFeeds = new ArrayList<>();
        for (UserDTO userDTO : userDTOList) {
            List<PostDTO> postDTOList = getPostOfUser(userDTO.getUserId());
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

    public boolean deletePost(Long postId, Long userId) {
        Optional<Post> post = postService.findByPostIdAndCreatorId(userId,postId);
        if (post.isPresent()) {
            postService.deletePost(post.get());
            return true;
        }
        return false;
    }

    public List<ActivityDTO> getActivitiesHistory(Long userId) {
        //Post
        List<ActivityDTO> activityDTOS = new ArrayList<>();
        List<PostDTO> postDTOList = postService.findByCreatorId(userId).stream()
                .map(post -> postMapper.postToDTO(post, false))
                .collect(Collectors.toList());
        activityDTOS.addAll(activityMapper.postToActivityDTO(
                postDTOList
        ));
        //PostReaction
        activityDTOS.addAll(activityMapper.postReactionToActivityDTO(
                postReactionMapper.postReactionListToDTO(
                        postReactionService.findByPostReactionIdUserId(userId)
                )
        ));
        //Comment
        activityDTOS.addAll(activityMapper.commentToActivityDTO(
                commentMapper.commentListToDTO(
                        commentService.findByUserId(userId)
                )
        ));
        //CommentReaction
        activityDTOS.addAll(activityMapper.commentReactionToActivityDTO(
                commentReactionMapper.commentReactionListToDTO(
                        commentReactionService.findByCommentReactionIdUserId(userId)
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


    public List<PostDTO> searchPosts(String searchStr) {
        return postService.findBySearchString(searchStr.toUpperCase()).stream()
                .map(post -> postMapper.postToDTO(post, false))
                .collect(Collectors.toList());
    }

    private Set<Long> getUserIdSet(List<Post> listPost) {
        Set<Long> listUserId = new HashSet<>();
        for (Post post : listPost) {
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
