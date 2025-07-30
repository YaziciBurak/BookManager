package com.example.bookapi.controller;

import com.example.bookapi.constants.CookieConstants;
import com.example.bookapi.constants.SuccessMessages;
import com.example.bookapi.dto.LoginRequest;
import com.example.bookapi.dto.RefreshTokenRequest;
import com.example.bookapi.dto.RegisterRequest;
import com.example.bookapi.dto.AuthResponse;
import com.example.bookapi.service.*;
import com.example.bookapi.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtService jwtService;
    private final TokenCookieService tokenCookieService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request);

        Cookie refreshCookie = tokenCookieService.createRefreshTokenCookie(authResponse.getRefreshToken());
        response.addCookie(refreshCookie);

        AuthResponse responseWithoutRefresh = new AuthResponse(authResponse.getAccessToken(), null);
        return ResponseEntity.ok(responseWithoutRefresh);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        Optional<Cookie> refreshCookie = CookieUtils.getCookie(request, CookieConstants.REFRESH_TOKEN);

        if(refreshCookie.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String refreshToken = refreshCookie.get().getValue();
        AuthResponse response = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String authHeader,
                                        @RequestBody RefreshTokenRequest request,
                                        HttpServletResponse response) {

        refreshTokenService.deleteByToken(request.getRefreshToken());
        String token = authHeader.replace("Bearer ", "").trim();
        Date expirationDate = jwtService.extractExpiration(token);
        tokenBlacklistService.blacklistToken(token, expirationDate.toInstant());

        tokenCookieService.clearRefreshTokenCookie(response);

        return ResponseEntity.ok(SuccessMessages.LOGOUT_SUCCESS);
    }

    @GetMapping("/refresh-token-cookie")
    public ResponseEntity<AuthResponse> refreshTokenFromCookie(
            @CookieValue(name ="refreshToken", required = false) String refreshToken
    ) {
        if(refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        AuthResponse authResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(authResponse);
    }
}
