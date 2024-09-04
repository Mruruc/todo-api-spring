package com.mruruc.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {
    @Value("${application.security.jwt.signing-key}")
    private String signingKey;
    @Value("${application.security.jwt.TOKEN_EXPIRATION_DATE}")
    private long tokenExpirationDate;

    private final UserDetailsService userDetailsService;

    public JwtService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public UserDetails validateToken(String jwtToken) {
        Claims claims = extractClaims(jwtToken);
        return usernameValidation(claims.getSubject());
    }

    private boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    private UserDetails usernameValidation(String subject) {
        return userDetailsService.loadUserByUsername(subject);
    }

    private Claims extractClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(signingKey));
    }

    public String generateToken(String subject) {
        return buildToken(subject, List.of(), Map.of());
    }

    public String generateToken(String subject, List<? extends GrantedAuthority> authorities) {
        return buildToken(subject, authorities, Map.of());
    }

    public String generateToken(String subject,
                                List<? extends GrantedAuthority> authorities,
                                Map<String, Object> extraClaims) {
        return buildToken(subject, authorities, extraClaims);
    }

    private String buildToken(String subject,
                              List<? extends GrantedAuthority> authorities,
                              Map<String, Object> extraClaims) {
        return Jwts.builder()
                .issuedAt(new Date())
                .subject(subject)
                .claim("authorities", authorities)
                .claims(extraClaims)
                .expiration(getTokenExpirationDate())
                .signWith(secretKey())
                .compact();
    }

    private Date getTokenExpirationDate() {
        return new Date(System.currentTimeMillis() + tokenExpirationDate);
    }
}
