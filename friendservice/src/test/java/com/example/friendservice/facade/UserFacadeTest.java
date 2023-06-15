package com.example.friendservice.facade;

import com.example.friendservice.dto.AuthDTO;
import com.example.friendservice.dto.RegisterUserDTO;
import com.example.friendservice.dto.UserDTO;
import com.example.friendservice.entity.ResetPasswordToken;
import com.example.friendservice.entity.User;
import com.example.friendservice.entity.VerificationToken;
import com.example.friendservice.repository.UserRepository;
import com.example.friendservice.repository.VerificationTokenRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserFacadeTest {
    private List<User> users = new ArrayList<>();
    private List<VerificationToken> verificationTokens = new ArrayList<>();
    private List<ResetPasswordToken> resetPasswordTokens = new ArrayList<>();
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

        VerificationToken verificationToken1 = VerificationToken.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .expirationTime(new Date(new Date().getTime()+1000*60*60))
                .user(users.get(0))
                .build();
        VerificationToken verificationToken2 = VerificationToken.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .expirationTime(new Date(new Date().getTime()+1000*60*60))
                .user(users.get(1))
                .build();
        verificationTokens = List.of(verificationToken1,verificationToken2);

        ResetPasswordToken resetPasswordToken1 = ResetPasswordToken.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .expirationTime(new Date(new Date().getTime()+1000*60*60))
                .user(users.get(0))
                .build();
        ResetPasswordToken resetPasswordToken2 = ResetPasswordToken.builder()
                .id(2L)
                .token(UUID.randomUUID().toString())
                .expirationTime(new Date(new Date().getTime()+1000*60*60))
                .user(users.get(1))
                .build();
        resetPasswordTokens = List.of(resetPasswordToken1,resetPasswordToken2);
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
        Mockito.when(userService.findByEmail(anyString()))
                .thenAnswer(invocation -> {
                    String email = invocation.getArgument(0);
                    List<User> foundUser = users.stream().filter(user -> user.getEmail().compareToIgnoreCase(email)==0)
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
        Mockito.doNothing().when(userService).saveVerificationToken(any(VerificationToken.class));
        Mockito.when(userService.findVerificationTokenByToken(anyString()))
                .thenAnswer(invocation -> {
                   String token = invocation.getArgument(0);
                   List<VerificationToken> tokens = verificationTokens.stream()
                           .filter(verificationToken -> verificationToken.getToken().compareTo(token)==0)
                           .collect(Collectors.toList());
                   if (tokens.size()>0){
                       return Optional.of(tokens.get(0));
                   }
                   return Optional.empty();
                });
        Mockito.when(userService.updateUserEnableById(anyLong()))
                .thenReturn(1);
        Mockito.doNothing().when(userService).deleteVerificationToken(any(VerificationToken.class));
        Mockito.when(userService.findResetPasswordTokenByToken(anyString()))
                .thenAnswer(invocation -> {
                    String token = invocation.getArgument(0);
                    List<ResetPasswordToken> tokens = resetPasswordTokens.stream()
                            .filter(resetPasswordToken -> resetPasswordToken.getToken().compareTo(token)==0)
                            .collect(Collectors.toList());
                    if (tokens.size()>0){
                        return Optional.of(tokens.get(0));
                    }
                    return Optional.empty();
                });
        Mockito.doNothing().when(userService).saveResetPasswordToken(any(ResetPasswordToken.class));
        Mockito.when(userService.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    return user;
                });
        Mockito.doNothing().when(userService).deleteResetPasswordToken(any(ResetPasswordToken.class));
        Mockito.when(userService.findAllById(anySet()))
                .thenAnswer(invocation -> {
                   Set<Long> setUserId = invocation.getArgument(0);
                   List<User> userList = users.stream()
                           .filter(user -> setUserId.contains(user.getUserId()))
                           .collect(Collectors.toList());
                   return userList;
                });
        Mockito.when(userService.findBySearchString(anyString()))
                .thenAnswer(invocation -> {
                    String searchStr = invocation.getArgument(0);
                    return userRepository.findBySearchString(searchStr);
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
    void whenPasswordAndConfirmPasswordNotMatch_throwBadRequest() {
        String email = "test@testemail.com";
        String phoneNum = randomPhoneNumber();
        RegisterUserDTO registerForm = RegisterUserDTO.builder()
                .firstName("Minh")
                .lastName("Nguyen Trong")
                .email(email)
                .phoneNum(phoneNum)
                .password("123456")
                .confirmPassword("1234567")
                .sex(true)
                .birthday(LocalDate.parse("2000-05-11"))
                .build();
        ResponseStatusException e = assertThrows(ResponseStatusException.class,()->{
            userFacade.registerUser(registerForm);
        });
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
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

    @Test
    void whenAccountNameOrPasswordInvalid_thenReturnEmpty() {
        User user = new User();
        user.setEmail( users.get(0).getEmail()+"abc");
        user.setPhoneNum("0");
        user.setPassword("123456");
        Optional<AuthDTO> actual = userFacade.login(user);
        assertEquals(Optional.empty(),actual);
    }

    @Test
    public void saveUserVerificationToken() {
        String token = UUID.randomUUID().toString();
        userFacade.saveUserVerificationToken(token,users.get(0));
    }

    @Test
    public void whenVerificationTokenInvalid_throwNotFoundException() {
        String invalidToken = UUID.randomUUID().toString();
        ResponseStatusException verifyRegistrationException =assertThrows(ResponseStatusException.class,()->{
            userFacade.verifyRegistration(invalidToken,invalidToken);
        });
        assertEquals(HttpStatus.NOT_FOUND,verifyRegistrationException.getStatusCode());

        ResponseStatusException getUserException =assertThrows(ResponseStatusException.class,()->{
            userFacade.getUserByVerificationToken(invalidToken);
        });
        assertEquals(HttpStatus.NOT_FOUND,getUserException.getStatusCode());
    }

    @Test
    public void whenVerificationTokenExpire_thenSaveNewToken() {
        verificationTokens.get(0).setExpirationTime(new Date(new Date().getTime()-60000));
        String expireToken = verificationTokens.get(0).getToken();
        String actual = userFacade.verifyRegistration(expireToken,UUID.randomUUID().toString());

        String expect = userFacade.TOKEN_EXPIRE_MESSAGE;
        assertEquals(expect,actual);
    }

    @Test
    public void whenVerificationTokenValid_returnAccountVerified() {
        String expireToken = verificationTokens.get(0).getToken();
        String actual = userFacade.verifyRegistration(expireToken,UUID.randomUUID().toString());

        String expect = userFacade.ACCOUNT_VERIFIED_MESSAGE;
        assertEquals(expect,actual);
    }

    @Test
    public void whenEmailInvalid_throwNotFoundException(){
        String invalidEmail = users.get(0).getEmail()+"abc";
        ResponseStatusException e = assertThrows(ResponseStatusException.class,()-> {
            userFacade.saveResetPasswordToken(invalidEmail, UUID.randomUUID().toString());
        });
        assertEquals(HttpStatus.NOT_FOUND,e.getStatusCode());
    }

    @Test
    public void whenEmailValid_returnResetPasswordTokenCreatedMessage(){
        String email = users.get(0).getEmail();
        String actual = userFacade.saveResetPasswordToken(email, UUID.randomUUID().toString());
        String expect = userFacade.RESET_PASSWORD_TOKEN_CREATED_MESSAGE;
        assertEquals(expect,actual);
    }

    @Test
    public void whenResetPasswordTokenInvalid_throwNotFoundException(){
        String invalidToken = UUID.randomUUID().toString();
        ResponseStatusException e = assertThrows(ResponseStatusException.class,()-> {
            userFacade.changePassword("new password", invalidToken);
        });
        assertEquals(HttpStatus.NOT_FOUND,e.getStatusCode());
    }

    @Test
    public void whenResetPasswordTokenValid_thenPasswordChangeSuccessfully(){
        String token = resetPasswordTokens.get(0).getToken();
        String expect = userFacade.PASSWORD_CHANGED_SUCCESSFULLY;
        String actual = userFacade.changePassword("new password", token);
        assertEquals(expect,actual);
    }

    @Test
    public void whenVerificationTokenValid_thenReturnUser() {
        String token = verificationTokens.get(0).getToken();
        Long expectUserId = verificationTokens.get(0).getUser().getUserId();

        User actual = userFacade.getUserByVerificationToken(token);
        assertEquals(expectUserId,actual.getUserId());
    }

    @Test
    public void whenUserIdValid_thenReturnUserDTO(){
        Long userId = users.get(0).getUserId();
        UserDTO actual = userFacade.getUserInfo(userId);

        assertEquals(userId, actual.getUserId());
    }

    @Test
    public void whenUserIdInvalid_throwNotFoundException(){
        Long invalidUserId = getMaxUserId(users);
        ResponseStatusException e = assertThrows(ResponseStatusException.class,()->{
            userFacade.getUserInfo(invalidUserId);
        });

        assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
    }

    @Test
    public void getListUserDetail(){
        Set<Long> setUserId = Set.of(users.get(0).getUserId(),users.get(1).getUserId());
        Map<Long,UserDTO> actual = userFacade.getListUserDetail(setUserId);

        assertEquals(setUserId.size(),actual.size());
        assertTrue(()->{
            boolean rs = true;
            for (Long userId:setUserId){
                if (!actual.containsKey(userId)) rs = false;
            }
            return rs;
        });
    }

    @Test
    public void searchUserBySearchString(){
        String searchStr = "Van B";
        List<UserDTO> userDTOList = userFacade.searchUsers(searchStr);
        assertTrue(()->{
            for (UserDTO user:userDTOList) {
                String userInfoStr = (user.getEmail()+" "+user.getPhoneNum()+" "+user.getLastName()+" "+user.getFirstName()).toUpperCase();
                if (!userInfoStr.contains(searchStr.toUpperCase())) return false;
            }
            return true;
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

}