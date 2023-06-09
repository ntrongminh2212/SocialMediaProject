package com.example.postservice.exception;

import com.example.postservice.dto.ResponseDTO;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({MissingRequestHeaderException.class})
    public ResponseEntity<Object> missingUserIdHeaderException(Exception e) {

        return new ResponseEntity<>(ResponseDTO.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ParseException.class})
    public ResponseEntity<Object> badRequestException(Exception e) {

        return ResponseEntity.badRequest().body(ResponseDTO.BADREQUEST);
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<Object> responseStatusException(ResponseStatusException e){
        if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
            return new ResponseEntity<>(ResponseDTO.FORBIDDEN, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(ResponseDTO.NOTFOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e) {

        logger.info(e);

        return ResponseEntity.status(500).body("Unknow error");
    }
}
