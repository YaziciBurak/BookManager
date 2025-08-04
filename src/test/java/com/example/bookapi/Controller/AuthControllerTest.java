package com.example.bookapi.Controller;

import com.example.bookapi.config.NoSecurityConfig;
import com.example.bookapi.controller.AuthController;
import com.example.bookapi.dto.AuthResponse;
import com.example.bookapi.dto.LoginRequest;
import com.example.bookapi.security.JwtAuthenticationFilter;
import com.example.bookapi.service.*;
import com.example.bookapi.util.AuthTestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
@Import(NoSecurityConfig.class)
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private  MockMvc mockMvc;

    @Autowired
    private  ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenCookieService tokenCookieService;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private TokenBlacklistService tokenBlacklistService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;



    @Test
    void loginSuccess() throws Exception {
        LoginRequest loginRequest = AuthTestDataFactory.createLoginRequest("testuser", "password123");
        AuthResponse authResponse = new AuthResponse("access-token", "refresh-token");

        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);
        Cookie refreshCookie = new Cookie("refreshToken", "refresh-token");
        when(tokenCookieService.createRefreshTokenCookie(anyString())).thenReturn(refreshCookie);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("refreshToken"))
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").doesNotExist());

        verify(authService).login(any(LoginRequest.class));
        verify(tokenCookieService).createRefreshTokenCookie("refresh-token");
    }

    @Test
    void loginBadRequest_whenInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refreshTokenFromCookieSuccess() throws Exception {
        AuthResponse authResponse = new AuthResponse("new-access-token", "new-refresh-token");

        when(authService.refreshToken(anyString())).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/refresh-token-cookie")
                        .cookie(new Cookie("refreshToken", "old-refresh-token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));

        verify(authService).refreshToken("old-refresh-token");
    }

    @Test
    void refreshTokenFromCookieBadRequest_whenNoCookie() throws Exception {
        mockMvc.perform(post("/api/auth/refresh-token-cookie"))
                .andExpect(status().isBadRequest());
    }
}
