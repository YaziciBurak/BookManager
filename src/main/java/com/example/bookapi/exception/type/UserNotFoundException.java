package com.example.bookapi.exception.type;

import com.example.bookapi.constants.ErrorMessages;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String username) {
        super(String.format(ErrorMessages.USER_NOT_FOUND, username));
    }
}
