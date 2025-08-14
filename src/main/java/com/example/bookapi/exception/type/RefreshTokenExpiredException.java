package com.example.bookapi.exception.type;

import com.example.bookapi.constants.ErrorMessages;

public class RefreshTokenExpiredException extends RuntimeException{
    public RefreshTokenExpiredException() {
        super(ErrorMessages.REFRESH_TOKEN_EXPIRED);
    }
}
