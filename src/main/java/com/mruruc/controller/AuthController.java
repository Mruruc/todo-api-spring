package com.mruruc.controller;

import com.mruruc.dto.AuthenticationRequest;
import com.mruruc.dto.AuthenticationResponse;
import com.mruruc.dto.RegistrationRequest;
import com.mruruc.security.service.AuthService;
import com.mruruc.security.service.RefreshTokenService;
import com.mruruc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/authenticate")
    public ResponseEntity<Void> authenticateUser(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authResponse =
                authService.authenticateRequest(request);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.accessToken())
                .header(HttpHeaders.SET_COOKIE, setCookie(authResponse.refreshToken(), 7 * 24 * 60 * 60))
                .build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody RegistrationRequest registrationRequest) {
        userService.saveUser(registrationRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshAccessToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {
        AuthenticationResponse authResponse =
                authService.refreshToken(refreshToken);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.accessToken())
                .build();
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.ok().build();
        }
        refreshTokenService.deleteToken(refreshToken);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, this.setCookie("", 0L))
                .build();
    }

    private String setCookie(String cookieValue, long maxAge) {
        return ResponseCookie.from("refreshToken", cookieValue)
                .httpOnly(true)
                .secure(true) //  The cookie is only sent over HTTPS.
                .path(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/auth")
                        .build()
                        .toUriString())
                .maxAge(maxAge)
                .sameSite("Strict")
                .build().toString();
    }


}
