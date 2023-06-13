package com.example.postservice.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class LoggingAspect {
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
        System.out.println("Time consume: "+ timeConsume + "milisecond");
        System.out.println("Return: "+ retValue.toString());
    }
}
