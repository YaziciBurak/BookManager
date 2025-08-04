package com.example.bookapi.service;

import com.example.bookapi.constants.ErrorMessages;
import com.example.bookapi.dto.AuthResponse;
import com.example.bookapi.dto.LoginRequest;
import com.example.bookapi.dto.RegisterRequest;
import com.example.bookapi.entity.RefreshToken;
import com.example.bookapi.entity.Role;
import com.example.bookapi.entity.User;
import com.example.bookapi.enums.RoleType;
import com.example.bookapi.exception.InvalidRefreshTokenException;
import com.example.bookapi.exception.UserNotFoundException;
import com.example.bookapi.exception.UsernameAlreadyExistsException;
import com.example.bookapi.repository.RoleRepository;
import com.example.bookapi.repository.UserRepository;
import com.example.bookapi.util.AuthTestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterRequest registerRequest = AuthTestDataFactory.createRegisterRequest("testuser", "password123");
        User user = AuthTestDataFactory.createUser(registerRequest.getUsername());
        Role role = new Role();
        role.setName(RoleType.ROLE_USER);

        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findByName(RoleType.ROLE_USER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user.getUsername())).thenReturn("access-token");
        when(refreshTokenService.createRefreshToken(user.getUsername())).thenReturn(
                AuthTestDataFactory.createRefreshToken(user)
        );

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = AuthTestDataFactory.createLoginRequest("testuser", "password123");
        User user = AuthTestDataFactory.createUser(loginRequest.getUsername());

        when(userRepository.findByUsername(loginRequest.getUsername()))
                .thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(jwtService.generateToken(loginRequest.getUsername())).thenReturn("access-token");
        when(refreshTokenService.createRefreshToken(loginRequest.getUsername()))
                .thenReturn(AuthTestDataFactory.createRefreshToken(user));

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @Test
    void testRefreshTokenSuccess() {
        User user = AuthTestDataFactory.createUser("testuser");
        RefreshToken oldToken = AuthTestDataFactory.createRefreshToken(user);
        RefreshToken newToken = AuthTestDataFactory.createRefreshToken(user);

        when(refreshTokenService.findByToken(oldToken.getToken()))
                .thenReturn(Optional.of(oldToken));
        when(refreshTokenService.verifyExpiration(oldToken)).thenReturn(oldToken);
        doNothing().when(refreshTokenService).deleteByToken(oldToken.getToken());
        when(refreshTokenService.createRefreshToken(user.getUsername())).thenReturn(newToken);
        when(jwtService.generateToken(user.getUsername())).thenReturn("access-token");

        AuthResponse response = authService.refreshToken(oldToken.getToken());

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @Test
    void testRegisterUsernameAlreadyExists() {
        RegisterRequest request = AuthTestDataFactory.createRegisterRequest("existingUser", "password123");
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(new User()));

        UsernameAlreadyExistsException exception = assertThrows(UsernameAlreadyExistsException.class,
                () -> authService.register(request));

        assertEquals(ErrorMessages.USERNAME_ALREADY_EXISTS, exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginUserNotFound() {
        LoginRequest request = AuthTestDataFactory.createLoginRequest("unknownUser", "password123");
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> authService.login(request)
        );

        assertEquals("User with username 'unknownUser' not found.", exception.getMessage());
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void testRefreshTokenInvalidToken() {
        String invalidToken = "invalid-token";
        when(refreshTokenService.findByToken(invalidToken)).thenReturn(Optional.empty());


        InvalidRefreshTokenException exception = assertThrows(
                InvalidRefreshTokenException.class,
                () -> authService.refreshToken(invalidToken)
        );

        assertEquals(ErrorMessages.INVALID_REFRESH_TOKEN, exception.getMessage());
    }
}
