package com.example.bookapi.service;

import com.example.bookapi.entity.BlacklistedToken;
import com.example.bookapi.repository.BlacklistedTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class TokenBlacklistService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public void blacklistToken(String token, Instant expirationDate) {
        if (!blacklistedTokenRepository.existsByToken(token)) {
            BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                    .token(token)
                    .expirationDate(expirationDate)
                    .build();

            blacklistedTokenRepository.save(blacklistedToken);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }
}
