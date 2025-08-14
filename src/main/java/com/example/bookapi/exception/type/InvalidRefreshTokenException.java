package com.example.bookapi.exception.type;

import com.example.bookapi.constants.ErrorMessages;

public class InvalidRefreshTokenException extends RuntimeException{
    public InvalidRefreshTokenException() {
        super(ErrorMessages.INVALID_REFRESH_TOKEN);
    }
}
