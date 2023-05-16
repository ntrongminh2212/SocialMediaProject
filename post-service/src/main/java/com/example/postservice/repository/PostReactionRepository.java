package com.example.postservice.repository;

import com.example.postservice.entity.PostReaction;
import com.example.postservice.id.PostReactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, PostReactionId> {

    Optional<List<PostReaction>> findByPostId(Long postId);
}
