package com.example.friendservice.repository;

import com.example.friendservice.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPhoneNum(String email, String phoneNum);

    User findByEmailOrPhoneNum(String email, String phoneNum);

    @Modifying
    @Transactional
    @Query(
            value = "update tbl_user set enable = 'true' where user_id = ?1 ",
            nativeQuery = true
    )
    int updateUserEnableById(Long userId);

    User findByEmail(String email);
}
