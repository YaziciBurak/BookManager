package com.example.bookapi.exception.type;

import com.example.bookapi.constants.ErrorMessages;

public class UsernameAlreadyExistsException extends RuntimeException{

    public UsernameAlreadyExistsException() {
        super(ErrorMessages.USERNAME_ALREADY_EXISTS);
    }
}
