package com.mruruc.security.service;

import com.mruruc.exceptions.InvalidTokenException;
import com.mruruc.security.model.RefreshToken;
import com.mruruc.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${application.security.opaque.refresh-token-expiration}")
    private long refreshTokenExpiration;
    private final RefreshTokenRepository repository;

    public RefreshToken findToken(String token) {
        return repository
                .findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));
    }

    public RefreshToken saveToken(String token, String username) {
        return repository.save(
                RefreshToken.builder()
                        .token(token)
                        .expiryDate(Instant.ofEpochMilli(System.currentTimeMillis() + refreshTokenExpiration))
                        .isRevoked(false)
                        .username(username)
                        .build()
        );
    }

    public void deleteToken(String token) {
        RefreshToken refreshToken = findToken(token);
        repository.delete(refreshToken);
    }

    public void deleteToken(RefreshToken token) {
        repository.delete(token);
    }

    public RefreshToken findTokenByEmail(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));
    }

}
