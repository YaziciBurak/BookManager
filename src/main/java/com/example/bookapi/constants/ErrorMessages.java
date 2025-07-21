package com.example.bookapi.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorMessages {
    public static final String USERNAME_ALREADY_EXISTS = "Username is already taken.";
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token.";
    public static final String PLEASE_LOGIN_FIRST = "Please login first.";
    public static final String ACCESS_DENIED = "You do not have permission to perform this action.";
    public static final String USER_NOT_FOUND = "Username not found.";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token has expired.";
    public static final String DEFAULT_ROLE_NOT_FOUND = "Default role not found.";
}
