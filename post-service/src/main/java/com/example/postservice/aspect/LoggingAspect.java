package com.example.postservice.aspect;

import com.example.postservice.dto.*;
import com.example.postservice.feignclient.UserClient;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Aspect
@Component
public class LoggingAspect {
    @Autowired
    private UserClient userClient;
    private Date timeStart;
    private Date timeEnd;
    @Before("execution(* com.example.postservice.facade.*.*(..))")
    public void beforeLogger(JoinPoint jp){
        System.out.println("-----"+jp.getSignature().getName()+"-----");
        timeStart = new Date();
    }

    @Pointcut("execution(* com.example.postservice.facade.*.*(..))")
    public void allFacadeMethodsPointCut(){}

    @AfterReturning(pointcut ="allFacadeMethodsPointCut()", returning = "retValue")
    public void returningLogger(Object retValue){
        timeEnd = new Date();
        Long timeConsume = timeEnd.getTime()-timeStart.getTime();
        if (retValue instanceof List){
            List<Object> objects = (List<Object>) retValue;
            for (Object oj:objects) {
//                setUserForObject(oj);
            }
        }else {
//            setUserForObject(retValue);
        }
        System.out.println("Time consume: "+ timeConsume + "milisecond");
        System.out.println("Return: "+ retValue.toString());
    }

    private void setUserForObject(Object retValue){
        if (retValue instanceof PostDTO){
            System.out.println("Instance of: PostDTO");
            PostDTO postDTO = (PostDTO) retValue;
            UserDTO userDTO = userClient.getUserInfo(postDTO.getUserId(),postDTO.getUserId()).get();
            postDTO.setUser(userDTO);
        }else if (retValue instanceof CommentDTO) {
            System.out.println("Instance of: CommentDTO");
            CommentDTO commentDTO = (CommentDTO) retValue;
            UserDTO userDTO = userClient.getUserInfo(commentDTO.getUserId(),commentDTO.getUserId()).get();
            commentDTO.setUser(userDTO);
        }else if (retValue instanceof PostReactionDTO) {
            System.out.println("Instance of: PostReactionDTO");
            PostReactionDTO postReactionDTO = (PostReactionDTO) retValue;
            UserDTO userDTO = userClient.getUserInfo(postReactionDTO.getUserId(),postReactionDTO.getUserId()).get();
            postReactionDTO.setUser(userDTO);
        }else if (retValue instanceof CommentReactionDTO) {
            System.out.println("Instance of: CommentReactionDTO");
            CommentReactionDTO commentReactionDTO = (CommentReactionDTO) retValue;
            UserDTO userDTO = userClient.getUserInfo(commentReactionDTO.getUserId(),commentReactionDTO.getUserId()).get();
            commentReactionDTO.setUser(userDTO);
        }
    }
}
