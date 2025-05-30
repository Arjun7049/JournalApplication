package com.journal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> userExceptionHandler(UserException ex){
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorMessage(ex.getMessage());
        errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.toString());
        errorInfo.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(errorInfo,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JournalException.class)
    public ResponseEntity<?> journalExceptionHandler(JournalException ex){
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorMessage(ex.getMessage());
        errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.toString());
        errorInfo.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(errorInfo,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> generalExceptionHandler(Exception ex){
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorMessage("Something went wrong");
        errorInfo.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        errorInfo.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(errorInfo,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
