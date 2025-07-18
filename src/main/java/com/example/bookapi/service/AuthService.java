package com.example.bookapi.service;

import com.example.bookapi.dto.LoginRequest;
import com.example.bookapi.dto.RegisterRequest;
import com.example.bookapi.dto.AuthResponse;
import com.example.bookapi.entity.RefreshToken;
import com.example.bookapi.entity.Role;
import com.example.bookapi.entity.User;
import com.example.bookapi.repository.RoleRepository;
import com.example.bookapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse register(RegisterRequest request) {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = User.builder().username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateToken(user.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(request.getUsername()).getToken();
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
        );
        String accessToken = jwtService.generateToken(request.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(request.getUsername()).getToken();
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        String newAccessToken = jwtService.generateToken(refreshToken.getUser().getUsername());
        return new AuthResponse(newAccessToken, requestRefreshToken);
    }
}
