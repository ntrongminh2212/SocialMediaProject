package com.example.postservice.repository;

import com.example.postservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<List<Post>> findByCreatorIdOrderByCreatedTimeDesc(Long userId);

    Optional<Post> findByPostIdAndCreatorId(Long postId, Long userId);
}
