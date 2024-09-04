package com.mruruc.controller;

import com.mruruc.dto.AuthenticationRequest;
import com.mruruc.dto.RegistrationRequest;
import com.mruruc.security.service.AuthService;
import com.mruruc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<Void> authenticateUser(@RequestBody AuthenticationRequest request) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authService.authenticateRequest(request));
        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody RegistrationRequest registrationRequest) {
        userService.saveUser(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
