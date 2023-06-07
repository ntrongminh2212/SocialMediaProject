package com.example.friendservice.exception;

import com.example.friendservice.dto.ResponseDTO;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = Logger.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler({MissingRequestHeaderException.class})
    public ResponseEntity<Object> missingUserIdHeaderException(Exception e) {

        return new ResponseEntity<>(ResponseDTO.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> globalException(Exception e) {
        logger.info(e);

        return ResponseEntity.status(500).body("Unknow error");
    }
}
