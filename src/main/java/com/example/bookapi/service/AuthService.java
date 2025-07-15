package com.example.bookapi.service;

import com.example.bookapi.dto.LoginRequest;
import com.example.bookapi.dto.RegisterRequest;
import com.example.bookapi.dto.AuthResponse;
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

    public AuthResponse register(RegisterRequest request) {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = User.builder().username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
        );
        String token = jwtService.generateToken(request.getUsername());
        return new AuthResponse(token);
    }
}
