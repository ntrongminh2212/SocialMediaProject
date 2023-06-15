package com.example.friendservice.exception;

import com.example.friendservice.dto.ResponseDTO;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({MissingRequestHeaderException.class})
    public ResponseEntity<Object> missingUserIdHeaderException(Exception e) {

        return new ResponseEntity<>(ResponseDTO.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<Object> responseStatusException(ResponseStatusException e) {
        logger.info("Response Exception");
        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            if (e.getReason() != null) {
                ResponseDTO.NOTFOUND.put("reason", e.getReason());
            }
            return new ResponseEntity(ResponseDTO.NOTFOUND, HttpStatus.NOT_FOUND);
        } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
            if (e.getReason() != null) {
                ResponseDTO.NOTFOUND.put("reason", e.getReason());
            }
            return new ResponseEntity(ResponseDTO.BADREQUEST, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.internalServerError().body(ResponseDTO.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> globalException(Exception e) {
        logger.info(e);

        return ResponseEntity.status(500).body("Unknow error");
    }
}
