package com.mruruc.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mruruc.handeler.exception_dto.ExceptionResponse;
import com.mruruc.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;


    public JwtAuthenticationFilter(JwtService jwtService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;

        this.objectMapper = objectMapper;
    }


    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {


        if (isAuthenticationEndpoint(request) || !isAuthorizationHeaderValid(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContext context = SecurityContextHolder.getContext();
        try {
            if (context.getAuthentication() == null) {
                authenticateRequest(request, context);
            }
        } catch (ExpiredJwtException | UsernameNotFoundException exception) {
            exceptionHandler(response, exception);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private <T extends RuntimeException> void exceptionHandler(HttpServletResponse response, T exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(
                objectMapper.writeValueAsString(
                        ExceptionResponse.builder()
                                .httpStatus(HttpStatus.UNAUTHORIZED)
                                .description(exception.getMessage())
                                .build()
                )
        );
    }


    private void authenticateRequest(HttpServletRequest request, SecurityContext context) {
        // extract jwt token
        String jwtToken = extractToken(request);
        // validate jwt token
        UserDetails userDetails = jwtService.validateToken(jwtToken);
        // create authentication token
        var authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // set to security context.
        context.setAuthentication(authenticationToken);
    }

    private String extractToken(HttpServletRequest request) {
        return getAuthHeader(request).substring(7);
    }

    private boolean isAuthorizationHeaderValid(HttpServletRequest request) {
        String authHeader = getAuthHeader(request);
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private String getAuthHeader(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    private boolean isAuthenticationEndpoint(HttpServletRequest request) {
        return request.getServletPath().contains("/auth/");
    }

}

