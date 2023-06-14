package com.example.friendservice.repository;

import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPhoneNum(String email, String phoneNum);

    Optional<User> findByEmailOrPhoneNum(String email, String phoneNum);

    @Modifying
    @Transactional
    @Query(
            value = "update tbl_user set enable = 'true' where user_id = ?1 ",
            nativeQuery = true
    )
    int updateUserEnableById(Long userId);

    User findByEmail(String email);

    @Query(
            value = "SELECT * " +
                    "FROM tbl_user, " +
                    "((SELECT target_id AS friend_id FROM friend " +
                    "WHERE friend.source_id = ?1) " +
                    "UNION " +
                    "(SELECT source_id AS friend_id FROM friend " +
                    "WHERE friend.target_id = ?1)) AS tbl_friend_id " +
                    "WHERE tbl_friend_id.friend_id = tbl_user.user_id",
            nativeQuery = true
    )
    List<User> findFriendByUserId(Long userId);

    @Query(
            value = "SELECT * \n" +
                    "FROM public.tbl_user \n" +
                    "WHERE UPPER(phone_num) LIKE %?1% \n" +
                    "OR UPPER(email) LIKE %?1% \n" +
                    "OR UPPER(concat(last_name,' ',first_name)) LIKE %?1%",
            nativeQuery = true
    )
    List<User> findBySearchString(String searchStr);
}
