package com.example.bookapi.exception.type;

import com.example.bookapi.constants.ErrorMessages;

public class InvalidLoginException extends RuntimeException {

    public InvalidLoginException() {
        super(ErrorMessages.INVALID_CREDENTIALS);
    }
}
