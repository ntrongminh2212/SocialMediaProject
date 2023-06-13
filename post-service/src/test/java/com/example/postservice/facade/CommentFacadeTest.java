package com.example.postservice.facade;

import com.example.postservice.dto.CommentDTO;
import com.example.postservice.entity.Comment;
import com.example.postservice.entity.Post;
import com.example.postservice.mapper.CommentMapper;
import com.example.postservice.mapper.CommentReactionMapper;
import com.example.postservice.service.CommentReactionService;
import com.example.postservice.service.CommentService;
import com.example.postservice.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CommentFacadeTest {

    @Autowired
    private CommentFacade commentFacade;
    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentReactionService reactionService;

    @MockBean
    private PostService postService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentReactionMapper commentReactionMapper;

    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @BeforeEach
    void setUp() throws ParseException {

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

        Mockito.when(commentService.save(any(Comment.class)))
                .thenAnswer(invocation -> {
                    Comment comment = invocation.getArgument(0);
                    comment.setCommentId(1L);
                    comment.setCommentReactions(new ArrayList<>());
                    comment.setCreateTime(dateTimeFormat.parse("12-06-2023 09:51:45"));
                    comment.setUpdateTime(dateTimeFormat.parse("12-06-2023 09:51:45"));
                    return comment;
                });
        Mockito.when(postService.findById(anyLong()))
                .thenAnswer(invocation -> {
                    Long postId = invocation.getArgument(0);
                    List<Post> foundPosts = posts.stream().filter(post -> post.getPostId() == postId).collect(Collectors.toList());
                    if (foundPosts.size()>0) {
                        return Optional.of(foundPosts.get(0));
                    }
                    return Optional.empty();
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
//        commentFacade.modifyComment();
    }

//    @Test
//    void deleteComment() {
//    }
//
//    @Test
//    void reactToComment() {
//    }
//
//    @Test
//    void findByUserId() {
//    }
}