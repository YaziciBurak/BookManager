package com.example.bookapi.controller;

import com.example.bookapi.constants.SuccessMessages;
import com.example.bookapi.dto.LoginRequest;
import com.example.bookapi.dto.RefreshTokenRequest;
import com.example.bookapi.dto.RegisterRequest;
import com.example.bookapi.dto.AuthResponse;
import com.example.bookapi.service.AuthService;
import com.example.bookapi.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request.getRefreshToken());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody RefreshTokenRequest request) {
        refreshTokenService.deleteByToken(request.getRefreshToken());
        return ResponseEntity.ok(SuccessMessages.LOGOUT_SUCCESS);
    }
}
