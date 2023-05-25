package com.example.postservice.repository;

import com.example.postservice.entity.CommentReaction;
import com.example.postservice.id.CommentReactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, CommentReactionId> {

}
