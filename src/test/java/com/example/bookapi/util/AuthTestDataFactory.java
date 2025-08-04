package com.example.bookapi.util;

import com.example.bookapi.dto.AuthResponse;
import com.example.bookapi.dto.LoginRequest;
import com.example.bookapi.dto.RegisterRequest;
import com.example.bookapi.entity.RefreshToken;
import com.example.bookapi.entity.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

public class AuthTestDataFactory {

    public static LoginRequest createLoginRequest(String username, String password) {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);
        return request;
    }

    public static RegisterRequest createRegisterRequest(String username, String password) {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword(password);
        return request;
    }

    public static User createUser(String username) {
        return User.builder()
                .id(1L)
                .username(username)
                .password("encodedPassword")
                .roles(new HashSet<>())
                .build();
    }

    public static RefreshToken createRefreshToken(User user) {
        return RefreshToken.builder()
                .token("refresh-token")
                .user(user)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();
    }

    public static AuthResponse createAuthResponse() {
        return new AuthResponse("access-token", "refresh-token");
    }
}
