package com.example.postservice.repository;

import com.example.postservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<List<Post>> findByCreatorIdOrderByCreatedTimeDesc(Long userId);

    Optional<Post> findByPostIdAndCreatorId(Long postId, Long userId);

    @Query(
            value = "select p \n" +
                    "from Post p \n" +
                    "join fetch p.comments pc \n" +
                    "where p.createdTime between ?1 and ?2 \n"
    )
    List<Post> findAllByCreatedTimeBetween(Date dayFrom, Date dayTo);

    List<Post> findByCreatorId(Long userId);

    @Query(
            value ="SELECT *\n " +
                    "FROM tbl_post\n " +
                    "WHERE UPPER(status_content) LIKE %?1% ",
            nativeQuery = true
    )
    List<Post> findBySearchString(String searchStr);
}
