package com.journal.exception;

import com.journal.entity.User;

public class UserException extends Exception{
    public UserException(String message){
        super(message);
    }
}
