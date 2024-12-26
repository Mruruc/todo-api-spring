package com.mruruc.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JwtService {
    @Value("${application.name}")
    private String tokenIssuer;
    @Value("${application.security.jwt.signing-key}")
    private String signingKey;
    @Value("${application.security.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    private final UserDetailsService userDetailsService;

    public UserDetails validateToken(String jwtToken) {
        Claims claims = extractClaims(jwtToken);
        return usernameValidation(claims.getSubject());
    }

    public String generateAccessToken(String subject) {
        return buildToken(subject, accessTokenExpiration, List.of(), Map.of());
    }

    public String generateAccessToken(String subject, List<? extends GrantedAuthority> authorities) {
        return buildToken(subject, accessTokenExpiration, authorities, Map.of());
    }

    public String generateAccessToken(String subject,
                                      List<? extends GrantedAuthority> authorities,
                                      Map<String, Object> extraClaims) {
        return buildToken(subject, accessTokenExpiration, authorities, extraClaims);
    }

    private String buildToken(String subject, long expirationMillis,
                              List<? extends GrantedAuthority> authorities,
                              Map<String, Object> extraClaims) {
        return Jwts.builder()
                .issuer(tokenIssuer)
                .issuedAt(new Date())
                .subject(subject)
                .claim("authorities", authorities)
                .claims(extraClaims)
                .expiration(getTokenExpirationDate(expirationMillis))
                .signWith(secretKey())
                .compact();
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

    private boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    private Date getTokenExpirationDate(long expirationMillis) {
        return new Date(System.currentTimeMillis() + expirationMillis);
    }

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(signingKey));
    }
}
