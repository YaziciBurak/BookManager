package com.example.bookapi.service;

import com.example.bookapi.constants.ErrorMessages;
import com.example.bookapi.dto.LoginRequest;
import com.example.bookapi.dto.RegisterRequest;
import com.example.bookapi.dto.AuthResponse;
import com.example.bookapi.entity.RefreshToken;
import com.example.bookapi.entity.Role;
import com.example.bookapi.entity.User;
import com.example.bookapi.enums.RoleType;
import com.example.bookapi.exception.InvalidRefreshTokenException;
import com.example.bookapi.exception.UserNotFoundException;
import com.example.bookapi.exception.UsernameAlreadyExistsException;
import com.example.bookapi.repository.RoleRepository;
import com.example.bookapi.repository.UserRepository;
import jakarta.transaction.Transactional;
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

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(ErrorMessages.USERNAME_ALREADY_EXISTS);
        }

        Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.DEFAULT_ROLE_NOT_FOUND));

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
        userRepository.findByUsername(request.getUsername())
                        .orElseThrow(() -> new UserNotFoundException(request.getUsername()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
        );
        String accessToken = jwtService.generateToken(request.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(request.getUsername()).getToken();
        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse refreshToken(String requestRefreshToken) {
        RefreshToken oldRefreshToken  = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException(ErrorMessages.INVALID_REFRESH_TOKEN));

        refreshTokenService.verifyExpiration(oldRefreshToken);

        refreshTokenService.deleteByToken(requestRefreshToken);

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(oldRefreshToken.getUser().getUsername());

        String newAccessToken = jwtService.generateToken(newRefreshToken.getUser().getUsername());

        return new AuthResponse(newAccessToken, newRefreshToken.getToken());
    }
}
