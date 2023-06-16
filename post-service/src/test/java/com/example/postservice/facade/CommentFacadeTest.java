package com.example.postservice.facade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import com.example.postservice.dto.CommentDTO;
import com.example.postservice.dto.CommentReactionDTO;
import com.example.postservice.dto.ResponseDTO;
import com.example.postservice.entity.Comment;
import com.example.postservice.entity.CommentReaction;
import com.example.postservice.entity.Post;
import com.example.postservice.id.CommentReactionId;
import com.example.postservice.mapper.CommentMapper;
import com.example.postservice.mapper.CommentMapperImpl;
import com.example.postservice.mapper.CommentReactionMapper;
import com.example.postservice.mapper.CommentReactionMapperImpl;
import com.example.postservice.service.CommentReactionService;
import com.example.postservice.service.CommentService;
import com.example.postservice.service.PostService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

@RunWith(MockitoJUnitRunner.class)
class CommentFacadeTest {

    @InjectMocks
    CommentFacade commentFacade = new CommentFacade();

    @Mock
    CommentService commentService;

    @Mock
    CommentReactionService reactionService;

    @Mock
    PostService postService;

    @Spy
    CommentMapper commentMapper = new CommentMapperImpl();

    @Spy
    CommentReactionMapper commentReactionMapper = new CommentReactionMapperImpl();

    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @BeforeEach
    void setUp() throws ParseException {
        ReflectionTestUtils.setField(
                commentMapper,
                "commentReactionMapper",
                commentReactionMapper
        );

        MockitoAnnotations.openMocks(this);
        Post post1 = Post.builder()
                .postId(1L)
                .creatorId(1l)
                .statusContent("Status 1")
                .postReactions(new ArrayList<>())
                .comments(new ArrayList<>())
                .attachmentUrl(
                        "https://res.cloudinary.com/minh2212/image/upload/v1686279541/socialmedia/post/lfi4mx34mqlsreu4glhe.jpg")
                .createdTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .updatedTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .isActive(true)
                .build();
        List<Post> posts = List.of(post1);

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
        post1.setComments(comments);

        CommentReaction commentReaction1 = CommentReaction.builder()
                .commentReactionId(new CommentReactionId(comment1, 2L))
                .reaction("LIKE")
                .reactedTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .build();
        CommentReaction commentReaction2 = CommentReaction.builder()
                .commentReactionId(new CommentReactionId(comment1, 1L))
                .reaction("HAHA")
                .reactedTime(dateTimeFormat.parse("12-06-2023 09:51:45"))
                .build();
        List<CommentReaction> commentReactions = List.of(commentReaction1, commentReaction2);
        comment1.setCommentReactions(commentReactions);

        Mockito.when(commentService.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            comment.setCommentId(1L);
            comment.setCommentReactions(new ArrayList<>());
            comment.setCreateTime(dateTimeFormat.parse("12-06-2023 09:51:45"));
            comment.setUpdateTime(dateTimeFormat.parse("12-06-2023 09:51:45"));
            return comment;
        });
        Mockito.when(postService.findById(anyLong())).thenAnswer(invocation -> {
            Long postId = invocation.getArgument(0);
            List<Post> foundPosts =
                    posts.stream().filter(post -> post.getPostId() == postId).collect(Collectors.toList());
            if (foundPosts.size() > 0) {
                return Optional.of(foundPosts.get(0));
            }
            return Optional.empty();
        });
        Mockito.when(commentService.findById(anyLong())).thenAnswer(invocation -> {
            Long commentId = invocation.getArgument(0);
            List<Comment> foundComment =
                    comments.stream().filter(comment -> comment.getCommentId() == commentId).collect(Collectors.toList());
            if (foundComment.size() > 0) {
                return Optional.of(foundComment.get(0));
            }
            return Optional.empty();
        });
        Mockito.when(commentService.findByCommentIdAndUserId(anyLong(), anyLong()))
                .thenAnswer(invocation -> {
                    Long commentId = invocation.getArgument(0);
                    Long userId = invocation.getArgument(1);
                    List<Comment> foundPosts = comments.stream()
                            .filter(comment -> comment.getUserId() == userId && comment.getCommentId() == commentId)
                            .collect(Collectors.toList());
                    if (foundPosts.size() > 0) {
                        return Optional.of(foundPosts.get(0));
                    }
                    return Optional.empty();
                });
        Mockito.doNothing().when(commentService).delete(any(Comment.class));
        Mockito.doNothing().when(reactionService).deleteAll(anyList());
        Mockito.when(reactionService.findById(anyLong(), anyLong()))
                .thenAnswer(invocation -> {
                    Long commentId = invocation.getArgument(0);
                    Long userId = invocation.getArgument(1);
                    List<CommentReaction> foundReaction = commentReactions.stream()
                            .filter(commentReaction -> commentReaction.getCommentReactionId().getUserId() == userId && commentReaction.getCommentReactionId().getComment().getCommentId() == commentId)
                            .collect(Collectors.toList());
                    if (foundReaction.size() > 0) {
                        return Optional.of(foundReaction.get(0));
                    }
                    return Optional.empty();
                });
        Mockito.when(reactionService.save(any(CommentReaction.class))).thenAnswer(invocation -> {
            CommentReaction commentReaction = invocation.getArgument(0);
            commentReaction.setReactedTime(dateTimeFormat.parse("12-06-2023 09:51:45"));
            return commentReaction;
        });
    }

    @Test
    void whenPostIdValid_saveCommentThenReturnNewCommentDTO() {
        Long expectPostId = 1L;
        CommentDTO commentDTO = CommentDTO.builder()
                .postId(expectPostId)
                .userId(1L)
                .content("Di Comment Dao")
                .build();
        CommentDTO actual = commentFacade.sendComment(commentDTO);
        assertEquals(expectPostId, actual.getPostId());
    }

    @Test
    void whenCommentIdAndUserIdValid_thenCommentShouldChanged() {
        Long commentId = 1L;
        Long userId = 2L;
        String expectContent = "Content change";
        CommentDTO commentDTO = CommentDTO.builder()
                .userId(userId)
                .commentId(commentId)
                .content(expectContent)
                .build();
        CommentDTO actual = commentFacade.modifyComment(commentDTO);
        assertEquals(commentId, actual.getCommentId());
        assertEquals(expectContent, actual.getContent());
    }

    @Test
    void whenCommentIdAndUserIdValid_thenDeleteCommentReturnTrue() {
        Long commentId = 1L;
        Long userId = 2L;
        CommentDTO commentDTO = CommentDTO.builder()
                .userId(userId)
                .commentId(commentId)
                .build();
        assertTrue(commentFacade.deleteComment(commentDTO));
    }

    @Test
    void whenCommentIdOrUserIdInvalid_thenCommentNotFoundReturnFalse() {
        Long commentId = 1L;
        Long userId = 2L;

        Long invalidCommentId = 0L;
        Long invalidUserId = 0L;
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setUserId(userId);
        commentDTO.setCommentId(invalidCommentId);
        assertFalse(commentFacade.deleteComment(commentDTO));

        commentDTO.setUserId(invalidUserId);
        commentDTO.setCommentId(commentId);
        assertFalse(commentFacade.deleteComment(commentDTO));
    }

    @Test
    void whenCommentIdValidAndReactionNotExist_thenAddReaction() {
        Long commentId = 1L;
        Long userId = 3L;
        CommentReactionDTO commentReactionDTO = CommentReactionDTO.builder()
                .commentId(commentId)
                .reaction("HAHA")
                .userId(userId)
                .build();
        CommentReactionDTO actual = commentFacade.reactToComment(commentReactionDTO);
        assertEquals(commentId,actual.getCommentId());
        assertEquals(userId,actual.getUserId());
    }

    @Test
    void whenCommentIdInvalid_thenThrowNotFoundException() {
        Long invalidCommentId = 3L;
        CommentReactionDTO commentReactionDTO = CommentReactionDTO.builder()
                .commentId(invalidCommentId)
                .reaction("HAHA")
                .userId(1L)
                .build();
        ResponseStatusException actual = assertThrows(ResponseStatusException.class,()->{
            commentFacade.reactToComment(commentReactionDTO);
        });
        assertEquals(HttpStatus.NOT_FOUND,actual.getStatusCode());
    }
}
