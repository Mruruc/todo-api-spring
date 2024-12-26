package com.mruruc.security.service;

import com.mruruc.dto.AuthenticationRequest;
import com.mruruc.dto.AuthenticationResponse;
import com.mruruc.exceptions.InvalidTokenException;
import com.mruruc.security.model.RefreshToken;
import com.mruruc.validation.dtoValidator.DtoValidator;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final DtoValidator<AuthenticationRequest> dtoValidator;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    public AuthenticationResponse authenticateRequest(AuthenticationRequest request) {
        dtoValidator.validate(request);

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String refreshToken = this.generateRefreshToken(request.email());
        String accessToken = jwtService.generateAccessToken(request.email());

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        String username = validateRefreshToken(refreshToken);
        String newJwtAccessToken = jwtService.generateAccessToken(username);
        return new AuthenticationResponse(newJwtAccessToken, refreshToken);
    }

    private String generateRefreshToken(String username) {
        RefreshToken refreshToken =
                refreshTokenService.saveToken(generateSecureRandom(), username);
        return refreshToken.getToken();
    }

    private String validateRefreshToken(String token) {
        if (token == null) throw new InvalidTokenException("Invalid refresh token");
        RefreshToken refreshToken = refreshTokenService.findToken(token);
        if (this.isRefreshTokenExpired(refreshToken)) {
            refreshTokenService.deleteToken(token);
            throw new InvalidTokenException("Invalid refresh token");
        }
        return refreshToken.getUsername();
    }

    public boolean isRefreshTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    private String generateSecureRandom() {
        var secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[64];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getEncoder().withoutPadding().encodeToString(tokenBytes);
    }

}
