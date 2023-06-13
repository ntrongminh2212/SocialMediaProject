package com.example.postservice.controller;

import com.example.postservice.dto.CommentDTO;
import com.example.postservice.dto.CommentReactionDTO;
import com.example.postservice.facade.CommentFacade;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/comment")
public class CommentController {
    @Autowired
    private CommentFacade commentFacade;

    Logger logger = Logger.getLogger(CommentController.class);

    @PostMapping("/send")
    public ResponseEntity<Object> sendComment(@RequestHeader Long userId, @RequestBody CommentDTO commentDTO) {
        commentDTO = commentFacade.sendComment(commentDTO);
        return ResponseEntity.ok(commentDTO);
    }

    @PutMapping("/modify")
    public ResponseEntity<CommentDTO> modifyComment(
            @RequestHeader Long userId, @RequestParam Long commentId, @RequestBody CommentDTO commentDTO) {
        commentDTO.setCommentId(commentId);
        CommentDTO rs = commentFacade.modifyComment(commentDTO);
        return ResponseEntity.ok(commentDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteComment(@RequestHeader Long userId, @RequestBody CommentDTO commentDTO) {
        boolean rs = commentFacade.deleteComment(commentDTO);
        if (rs) return ResponseEntity.ok(rs);

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/react-comment")
    public ResponseEntity<CommentReactionDTO> reactToComment(@RequestBody CommentReactionDTO reactionDTO) {
        reactionDTO = commentFacade.reactToComment(reactionDTO);
        return ResponseEntity.ok(reactionDTO);
    }
}
