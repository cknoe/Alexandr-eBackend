package com.cknoe.backend_springboot.security.jwt;

import java.util.Date;
import java.util.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Key key;
    private final Key refreshKey;
    private final int jwtExpirationMs;
    private final int jwtRefreshExpirationMs;

    public JwtUtil(
            @Value("${app.jwt-secret}") String secretKey,
            @Value("${app.jwt-expiration-ms}") int jwtExpirationMs,
            @Value("${app.jwt-refresh-secret}") String secretRefreshKey,
            @Value("${app.jwt-refresh-expiration-ms}") int jwtRefreshExpirationMs) {

        System.out.println("JWT SECRET = [" + secretKey + "]");
        System.out.println("JWT EXP MS = [" + jwtExpirationMs + "]");
        System.out.println("JWT SECRET REFRESH = [" + secretRefreshKey + "]");
        System.out.println("JWT SECRET REFRESH EXP MS = [" + jwtRefreshExpirationMs + "]");
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        byte[] decodedRefreshKey = Base64.getDecoder().decode(secretRefreshKey);
        this.key = Keys.hmacShaKeyFor(decodedKey);
        this.jwtExpirationMs = jwtExpirationMs;
        this.refreshKey = Keys.hmacShaKeyFor(decodedRefreshKey);
        this.jwtRefreshExpirationMs = jwtRefreshExpirationMs;
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.jwtExpirationMs))
                .signWith(this.key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.jwtRefreshExpirationMs))
                .signWith(this.refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsernameFromToken(String token) {
        return extractUsername(token, this.key);
    }

    public String extractUsernameFromRefresh(String token) {
        return extractUsername(token, this.refreshKey);
    }

    public String extractUsername(String token, Key key) {
        return parseClaims(token, key)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsernameFromToken(token).equals(userDetails.getUsername());
    }

    public boolean validateRefreshToken(String token) {
        try {
            parseClaims(token, this.refreshKey);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Jws<Claims> parseClaims(String token, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}