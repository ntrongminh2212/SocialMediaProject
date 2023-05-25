package com.example.postservice.controller;

import com.example.postservice.dto.CommentDTO;
import com.example.postservice.dto.CommentReactionDTO;
import com.example.postservice.entity.CommentReaction;
import com.example.postservice.service.CommentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    Logger logger = Logger.getLogger(CommentController.class);

    @PostMapping("/send")
    public ResponseEntity<CommentDTO> sendComment(@RequestBody CommentDTO commentDTO){
        commentDTO = commentService.sendComment(commentDTO);
        return ResponseEntity.ok(commentDTO);
    }

    @PutMapping("/modify")
    public ResponseEntity<CommentDTO> modifyComment(
            @RequestParam Long commentId,
            @RequestBody CommentDTO commentDTO
    ){
        commentDTO.setCommentId(commentId);
        CommentDTO rs = commentService.modifyComment(commentDTO);
        return ResponseEntity.ok(commentDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteComment(@RequestBody CommentDTO commentDTO){
         boolean rs = commentService.deleteComment(commentDTO);
         if (rs) return ResponseEntity.ok(rs);

         return ResponseEntity.notFound().build();
    }

    @PostMapping("/react-comment")
    public ResponseEntity<CommentReactionDTO> reactToComment(@RequestBody CommentReactionDTO reactionDTO){
        reactionDTO = commentService.reactToComment(reactionDTO);
        return ResponseEntity.ok(reactionDTO);
    }
}
