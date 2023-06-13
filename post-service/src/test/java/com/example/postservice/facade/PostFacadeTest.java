package com.example.postservice.facade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import com.example.postservice.dto.*;
import com.example.postservice.entity.Comment;
import com.example.postservice.entity.CommentReaction;
import com.example.postservice.entity.Post;
import com.example.postservice.entity.PostReaction;
import com.example.postservice.feignclient.UserClient;
import com.example.postservice.id.CommentReactionId;
import com.example.postservice.id.PostReactionId;
import com.example.postservice.mapper.PostMapper;
import com.example.postservice.service.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class PostFacadeTest {

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private PostMapper postMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private CloudinaryService cloudinaryService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentReactionService commentReactionService;

    @MockBean
    private PostReactionService postReactionService;

    @MockBean
    private UserClient userClient;

    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @BeforeEach
    void setUp() throws ParseException {

        UserDTO userDTO1 = UserDTO.builder()
                .userId(1L)
                .firstName("Minh")
                .lastName("Nguyen Trong")
                .email("ntrongminh2212@gmail.com")
                .phoneNum("0123456789")
                .avatar(
                        "https://res.cloudinary.com/minh2212/image/upload/v1685933236/socialmedia/avatar/default-avatar-profile-icon-of-social-media-user-vector_fowxgz.jpg")
                .sex(true)
                .birthday(LocalDate.of(2000, 12, 22))
                .role(Role.USER)
                .build();

        UserDTO userDTO2 = UserDTO.builder()
                .userId(2L)
                .firstName("B")
                .lastName("Tran Van")
                .email("user01@gmail.com")
                .phoneNum("0123456781")
                .avatar(
                        "https://res.cloudinary.com/minh2212/image/upload/v1685933236/socialmedia/avatar/default-avatar-profile-icon-of-social-media-user-vector_fowxgz.jpg")
                .sex(true)
                .birthday(LocalDate.of(1999, 8, 22))
                .role(Role.USER)
                .build();

        Comment comment1 = Comment.builder()
                .commentId(1L)
                .userId(2L)
                .content("Di Comment dao")
                .createTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .updateTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .commentReactions(new ArrayList<>())
                .build();
        Comment comment2 = Comment.builder()
                .commentId(2L)
                .userId(1L)
                .content("Di Comment dao")
                .createTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .updateTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .commentReactions(new ArrayList<>())
                .build();
        List<Comment> comments = List.of(comment1, comment2);

        CommentReaction commentReaction1 = CommentReaction.builder()
                .commentReactionId(new CommentReactionId(comment1, userDTO2.getUserId()))
                .reaction("LIKE")
                .reactedTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .build();
        CommentReaction commentReaction2 = CommentReaction.builder()
                .commentReactionId(new CommentReactionId(comment1, userDTO1.getUserId()))
                .reaction("HAHA")
                .reactedTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .build();
        List<CommentReaction> commentReactions = List.of(commentReaction1, commentReaction2);
        comment1.setCommentReactions(commentReactions);

        Post post1 = Post.builder()
                .postId(1L)
                .creatorId(1l)
                .statusContent("Status 1")
                .postReactions(new ArrayList<>())
                .comments(List.of(comment1))
                .attachmentUrl(
                        "https://res.cloudinary.com/minh2212/image/upload/v1686279541/socialmedia/post/lfi4mx34mqlsreu4glhe.jpg")
                .createdTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .updatedTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .isActive(true)
                .build();
        Post post2 = Post.builder()
                .postId(2L)
                .creatorId(1l)
                .statusContent("Status 2")
                .postReactions(new ArrayList<>())
                .comments(new ArrayList<>())
                .attachmentUrl(
                        "https://res.cloudinary.com/minh2212/image/upload/v1686279541/socialmedia/post/lfi4mx34mqlsreu4glhe.jpg")
                .createdTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .updatedTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .isActive(true)
                .build();
        Post post3 = Post.builder()
                .postId(3L)
                .creatorId(2l)
                .statusContent("Status 2")
                .postReactions(new ArrayList<>())
                .comments(List.of(comment2))
                .attachmentUrl(
                        "https://res.cloudinary.com/minh2212/image/upload/v1686279541/socialmedia/post/lfi4mx34mqlsreu4glhe.jpg")
                .createdTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .updatedTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .isActive(true)
                .build();
        List<Post> posts = List.of(post1, post2, post3);

        PostReaction postReaction1 = PostReaction.builder()
                .postReactionId(new PostReactionId(post1, userDTO1.getUserId()))
                .reactedTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .reaction("LIKE")
                .build();
        List<PostReaction> postReactions = List.of(postReaction1);
        post1.setPostReactions(List.of(postReaction1));

        Mockito.when(postService.findByCreatorIdOrderByCreatedTimeDesc(1L))
                .thenReturn(
                        posts.stream().filter(post -> post.getCreatorId() == 1l).collect(Collectors.toList()));
        Mockito.when(cloudinaryService.uploadImage("base64img")).thenReturn(new HashMap<>() {
            {
                put("success", "true");
                put(
                        "url",
                        "https://res.cloudinary.com/minh2212/image/upload/v1686279541/socialmedia/post/lfi4mx34mqlsreu4glhe.jpg");
            }
        });
        Mockito.when(postService.createPost(any(Post.class))).thenAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            return postMapper.postToDTO(post, false);
        });

        Mockito.when(postService.findById(1L)).thenReturn(Optional.of(post1));
        Mockito.when(userClient.getListUserDetail(anySet())).thenReturn(new HashMap<>() {
            {
                put(userDTO1.getUserId(), userDTO1);
                put(userDTO2.getUserId(), userDTO2);
            }
        });
        Mockito.when(userClient.getListFriend(2L)).thenReturn(ResponseEntity.ok(List.of(userDTO1)));
        Mockito.when(userClient.getListFriend(0L)).thenReturn(ResponseEntity.ok(new ArrayList<>()));
        Mockito.when(postService.findByPostIdAndCreatorId(anyLong(), anyLong())).thenAnswer(invocation -> {
            Long userId = invocation.getArgument(0);
            Long postId = invocation.getArgument(0);
            List<Post> foundPost = posts.stream()
                    .filter(post -> post.getPostId() == postId && post.getCreatorId() == userId)
                    .collect(Collectors.toList());
            if (foundPost.size() > 0) {
                return Optional.of(foundPost.get(0));
            }
            return Optional.empty();
        });
        Mockito.doAnswer(invocation -> {
                    Post post = invocation.getArgument(0);
                    post.setActive(false);
                    return null;
                })
                .when(postService)
                .deletePost(post1);
        Mockito.when(postService.findByCreatorId(anyLong())).thenAnswer(invocation -> {
            Long userId = invocation.getArgument(0);
            List<Post> foundPost = new ArrayList<>();
            foundPost =
                    posts.stream().filter(post -> post.getCreatorId() == userId).collect(Collectors.toList());
            return foundPost;
        });
        Mockito.when(commentService.findByUserId(anyLong())).thenAnswer(invocation -> {
            Long userId = invocation.getArgument(0);
            List<Comment> foundComments = new ArrayList<>();
            foundComments = comments.stream()
                    .filter(comment -> comment.getUserId() == userId)
                    .collect(Collectors.toList());
            return foundComments;
        });
        Mockito.when(postReactionService.findByPostReactionIdUserId(anyLong())).thenAnswer(invocation -> {
            Long userId = invocation.getArgument(0);
            List<PostReaction> foundPostReactions = new ArrayList<>();
            foundPostReactions = postReactions.stream()
                    .filter(postReaction -> postReaction.getPostReactionId().getUserId() == userId)
                    .collect(Collectors.toList());
            return foundPostReactions;
        });
        Mockito.when(commentReactionService.findByCommentReactionIdUserId(anyLong()))
                .thenAnswer(invocation -> {
                    Long userId = invocation.getArgument(0);
                    List<CommentReaction> foundCommentReactions = new ArrayList<>();
                    foundCommentReactions = commentReactions.stream()
                            .filter(commentReaction ->
                                    commentReaction.getCommentReactionId().getUserId() == userId)
                            .collect(Collectors.toList());
                    return foundCommentReactions;
                });
        Mockito.when(postService.findBySearchString(anyString())).thenAnswer(invocation -> {
            String searchStr = invocation.getArgument(0);
            List<Post> foundPosts = posts.stream()
                    .filter(post -> post.getStatusContent().toUpperCase().contains(searchStr.toUpperCase()))
                    .collect(Collectors.toList());
            return foundPosts;
        });
    }

    @Test
    void whenUserIdIsValid_thenReturnPostsOfUserId() {
        Long userId = 1L;
        List<PostDTO> actual = postFacade.getPostOfUser(userId);
        assertTrue(() -> actual.stream().allMatch(postDTO -> postDTO.getUser().getUserId() == userId));
    }

    @Test
    void whenUserIdIsInvalid_thenReturnEmptyList() {
        List<PostDTO> expect = new ArrayList<>();
        List<PostDTO> actual = postFacade.getPostOfUser(0L);
        assertIterableEquals(expect, actual);
    }

    @Test
    void whenCreatePost_thenReturnNewPostDTO() {
        PostDTO postDTO = PostDTO.builder()
                .postId(1L)
                .userId(1l)
                .statusContent("Status 1")
                .attachmentUrl("base64img")
                .createdTime("12-06-2023 09:51:45")
                .updatedTime("12-06-2023 09:51:45")
                .postReactions(new ArrayList<>())
                .comments(new ArrayList<>())
                .postReactionsCount(0)
                .commentsCount(0)
                .build();
        PostDTO actual = postFacade.createPost(postDTO);
        postDTO.setAttachmentUrl(
                "https://res.cloudinary.com/minh2212/image/upload/v1686279541/socialmedia/post/lfi4mx34mqlsreu4glhe.jpg");
        assertEquals(postDTO.getPostId(), actual.getPostId());
        assertEquals(postDTO.getAttachmentUrl(), actual.getAttachmentUrl());
    }

    @Test
    void whenPostIdIsValid_thenReturnPostDetail() {
        Long expectPostId = 1L;
        int expectCommentCount = 1;
        Long expectUserId = 1L;

        Optional<PostDTO> actual = postFacade.getPostDetail(1L);
        assertEquals(expectPostId, actual.get().getPostId());
        assertEquals(expectCommentCount, actual.get().getCommentsCount());
        assertEquals(expectUserId, actual.get().getUser().getUserId());
    }

    @Test
    void whenPostIdIsInvalid_thenReturnEmptyOptional() {
        Optional<PostDTO> actual = postFacade.getPostDetail(0L);
        assertEquals(Optional.empty(), actual);
    }

    @Test
    void whenUserIdValid_thenReturnUserNewFeed() {
        Long userId = 2L;
        int expectPostCount = 2;
        List<PostDTO> actual = postFacade.getNewFeed(userId);
        assertEquals(expectPostCount, actual.size());
    }

    @Test
    void whenUserIdInValid_thenReturnEmptyList() {
        Long invalidUserId = 0L;
        List<PostDTO> expect = new ArrayList<>();
        List<PostDTO> actual = postFacade.getNewFeed(invalidUserId);
        assertEquals(expect, actual);
    }

    @Test
    void whenUserIdAndPostIdValid_deactivePostthenReturnTrue() {
        Long postId = 1L;
        Long userId = 1L;
        boolean actual = postFacade.deletePost(postId, userId);
        assertTrue(actual);
    }

    @Test
    void whenUserIdOrPostIdInvalid_thenPostStayAndReturnFalse() {
        Long postId = 1L;
        Long userId = 0L;
        boolean actual = postFacade.deletePost(postId, userId);
        assertFalse(actual);
    }

    @Test
    void whenUserIdValid_thenReturnUserActivities() {
        Long userId = 1L;
        List<ActivityDTO> actual = postFacade.getActivitiesHistory(userId);
        assertTrue(() -> actual.stream().allMatch(activityDTO -> {
            Long rs = 0L;
            if (activityDTO.getAction() instanceof PostDTO) {
                rs = ((PostDTO) activityDTO.getAction()).getUserId();
            } else if (activityDTO.getAction() instanceof PostReactionDTO) {
                rs = ((PostReactionDTO) activityDTO.getAction()).getUserId();
            } else if (activityDTO.getAction() instanceof CommentDTO) {
                rs = ((CommentDTO) activityDTO.getAction()).getUserId();
            } else if (activityDTO.getAction() instanceof CommentReactionDTO) {
                rs = ((CommentReactionDTO) activityDTO.getAction()).getUserId();
            }
            return rs == userId;
        }));
    }

    @Test
    void whenUserIdInValid_thenReturnEmptyListActivity() {
        Long userId = 0L;
        List<ActivityDTO> expect = new ArrayList<>();
        List<ActivityDTO> actual = postFacade.getActivitiesHistory(userId);
        assertEquals(expect, actual);
    }

    @Test
    void whenSearchPost_thenReturnPosts() {
        String searchStr = "content";
        List<PostDTO> actual = postFacade.searchPosts(searchStr);
        assertTrue(() ->
                actual.stream().allMatch(postDTO -> postDTO.getStatusContent().contains(searchStr)));
    }
}
