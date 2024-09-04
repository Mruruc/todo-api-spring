package com.mruruc.security.service;

import com.mruruc.dto.AuthenticationRequest;
import com.mruruc.validation.dtoValidator.DtoValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final DtoValidator<AuthenticationRequest> dtoValidator;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(DtoValidator<AuthenticationRequest> dtoValidator,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.dtoValidator = dtoValidator;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public String authenticateRequest(AuthenticationRequest request) {
        // validate request
        dtoValidator.validate(request);
        // authenticate the usernamePasswordAuthenticationToken
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        // set authentication to security context
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticate);
        // generate jwt token and return
        return jwtService.generateToken(request.getEmail());
    }

}
