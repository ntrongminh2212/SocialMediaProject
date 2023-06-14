package com.example.friendservice.facade;

import com.example.friendservice.dto.AuthDTO;
import com.example.friendservice.dto.RegisterUserDTO;
import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.User;
import com.example.friendservice.repository.UserRepository;
import com.example.friendservice.service.AuthenticationService;
import com.example.friendservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserFacadeTest {
    private List<User> users = new ArrayList<>();
    @Autowired
    UserFacade userFacade;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationService authenticationService;
    @MockBean
    UserService userService;

    @BeforeEach
    void setUp() {
        users = userRepository.findAll(PageRequest.of(0, 10)).toList();
        Long maxUserId = getMaxUserId(users);

        Mockito.when(userService.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setJoinedTime(new Date());
                    user.setUpdatedTime(new Date());
                    user.setUserId(maxUserId);

                    return user;
                });
        Mockito.when(userService.findByEmailOrPhoneNum(anyString(), anyString()))
                .thenAnswer(invocation -> {
                    String email = invocation.getArgument(0);
                    String phoneNum = invocation.getArgument(1);
                    List<User> foundUser = users.stream().filter(user -> user.getEmail().compareToIgnoreCase(email)==0 || user.getPhoneNum().compareTo(phoneNum)==0)
                            .collect(Collectors.toList());
                    if (foundUser.size()>0){
                        return Optional.of(foundUser.get(0));
                    }
                    return Optional.empty();
                });
        Mockito.when(userService.findById(anyLong()))
                .thenAnswer(invocation -> {
                    Long userId = invocation.getArgument(0);
                    List<User> foundUser = users.stream().filter(user -> user.getUserId() ==userId)
                            .collect(Collectors.toList());
                    if (foundUser.size()>0){
                        return Optional.of(foundUser.get(0));
                    }
                    return Optional.empty();
                });
    }

    @Test
    void whenEmailAndPhoneNumNotExist_thenSaveNewUser() {
        String email = "test@testemail.com";
        String phoneNum = randomPhoneNumber();
        RegisterUserDTO registerForm = RegisterUserDTO.builder()
                .firstName("Minh")
                .lastName("Nguyen Trong")
                .email(email)
                .phoneNum(phoneNum)
                .password("123456")
                .confirmPassword("123456")
                .sex(true)
                .birthday(LocalDate.parse("2000-05-11"))
                .build();
        Optional<User> actual = userFacade.registerUser(registerForm);
        assertEquals(email, actual.get().getEmail());
        assertEquals(phoneNum, actual.get().getPhoneNum());
    }

    @Test
    void whenEmailOrPhoneNumAlreadyExist_thenReturnEmpty() {
        String email = "test@testemail.com";
        String phoneNum = randomPhoneNumber();

        String existEmail = users.get(0).getEmail();
        String existPhoneNum = users.get(0).getPhoneNum();
        RegisterUserDTO registerForm = RegisterUserDTO.builder()
                .firstName("Minh")
                .lastName("Nguyen Trong")
                .password("123456")
                .confirmPassword("123456")
                .sex(true)
                .birthday(LocalDate.parse("2000-05-11"))
                .build();

        registerForm.setEmail(existEmail);
        registerForm.setPhoneNum(phoneNum);
        Optional<User> actual1 = userFacade.registerUser(registerForm);
        assertEquals(Optional.empty(), actual1);

        registerForm.setEmail(email);
        registerForm.setPhoneNum(existPhoneNum);
        Optional<User> actual2 = userFacade.registerUser(registerForm);
        assertEquals(Optional.empty(), actual2);
    }

    @Test
    void whenAccountNameAndPasswordValid_thenReturnToken() {
        User user = new User();
        user.setEmail( users.get(0).getEmail());
        user.setPhoneNum( users.get(0).getPhoneNum());
        user.setPassword("123456");
        Optional<AuthDTO> actual = userFacade.login(user);
        assertTrue(()->{
            if (actual.isPresent()) {
                Optional<UserDTO> userDTO = authenticationService.authenticateToken(actual.get().getAccessToken());
                if (userDTO.isPresent()) {
                    return userDTO.get().getEmail() == user.getEmail();
                }
                return false;
            }
            return false;
        });
    }

    private Long getMaxUserId(List<User> users) {
        return users.stream().max(Comparator.comparing(User::getUserId)).get().getUserId() + 1;
    }

    private String randomPhoneNumber() {
        StringBuilder phoneNum = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            phoneNum.append(rand.nextInt(10));
        }
        Optional<User> user = userService.findByEmailOrPhoneNum("abc", phoneNum.toString());
        if (user.isEmpty()) {
            return phoneNum.toString();
        }
        return randomPhoneNumber();
    }

//
//    @Test
//    void saveUserVerificationToken() {
//    }
//
//    @Test
//    void verifyRegistration() {
//    }
//
//    @Test
//    void saveResetPasswordToken() {
//    }
//
//    @Test
//    void changePassword() {
//    }
//
//    @Test
//    void getUserByVerificationToken() {
//    }
//
//    @Test
//    void getUserById() {
//    }
//
//    @Test
//    void getUserInfo() {
//    }
//
//    @Test
//    void getListUserDetail() {
//    }
//
//    @Test
//    void searchUsers() {
//    }
}