package com.example.friendservice.facade;

import com.example.friendservice.entity.Friend;
import com.example.friendservice.entity.Role;
import com.example.friendservice.entity.User;
import com.example.friendservice.idkey.FriendId;
import com.example.friendservice.mapper.FriendMapper;
import com.example.friendservice.mapper.FriendMapperImpl;
import com.example.friendservice.service.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class FriendFacadeTest {
    private List<User> users = new ArrayList<>();
    private List<Friend> friends = new ArrayList<>();

    @InjectMocks
    FriendFacade friendFacade = new FriendFacade();
    @Spy
    FriendMapper friendMapper = new FriendMapperImpl();
    @Mock
    FriendService friendService;
    @BeforeEach
    void setUp() {
        User user1 = User.builder()
                .userId(1L)
                .firstName("Minh")
                .lastName("Nguyen Trong")
                .email("ntrongminh2212@gmail.com")
                .phoneNum("0123456789")
                .avatar("abcd")
                .password("123456")
                .role(Role.ADMIN)
                .sex(true)
                .birthday(LocalDate.of(2000,12,22))
                .enable(true)
                .joinedTime(new Date())
                .updatedTime(new Date())
                .build();
        User user2 = User.builder()
                .userId(2L)
                .firstName("B")
                .lastName("Tran Van")
                .email("sm.user01@gmail.com")
                .phoneNum("0123456781")
                .avatar("abcd")
                .password("123456")
                .role(Role.USER)
                .sex(true)
                .birthday(LocalDate.of(1999,4,12))
                .enable(true)
                .joinedTime(new Date())
                .updatedTime(new Date())
                .build();
        User user3 = User.builder()
                .userId(3L)
                .firstName("C")
                .lastName("Le Thi")
                .email("sm.user02@gmail.com")
                .phoneNum("0123456181")
                .avatar("abcd")
                .password("123456")
                .role(Role.USER)
                .sex(true)
                .birthday(LocalDate.of(1999,4,12))
                .enable(true)
                .joinedTime(new Date())
                .updatedTime(new Date())
                .build();
        users = List.of(user1,user2,user3);
        Friend friend1 = Friend.builder()
                .friendId(new FriendId(user1,user2))
                .sentDate(new Date())
                .isAccepted(false)
                .isAccepted(false)
                .isBlock(false)
                .build();
        Friend friend2 = Friend.builder()
                .friendId(new FriendId(user1,user3))
                .sentDate(new Date())
                .isAccepted(false)
                .isAccepted(false)
                .isBlock(false)
                .build();
        Friend friend3 = Friend.builder()
                .friendId(new FriendId(user2,user3))
                .sentDate(new Date())
                .isAccepted(false)
                .isAccepted(false)
                .isBlock(false)
                .build();
        friends = List.of(friend1,friend2,friend3);

//        friendService.findById
//        friendService.save
    }

    @Test
    void requestFriend() {
    }

    @Test
    void acceptFriendRequest() {
    }

    @Test
    void getFriendList() {
    }
}