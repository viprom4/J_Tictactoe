package com.example.J_Tictactoe.web.security;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.SecretKey;

import com.example.J_Tictactoe.domain.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtProvider {
    private static final String SECRET = "voZWxfUEtInDeaxGf9tlw7bVOkifHhwFuo3YbTy0GoZ";

    private final SecretKey key;

    public JwtProvider() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String accessToken(User user) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                    .subject(user.getId().toString())
                    .claim("roles", user.getRoles())
                    .expiration(date)
                    .signWith(key)
                    .compact();
    }

    public String refreshToken(User user) {
        Date date = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                    .subject(user.getId().toString())
                    .expiration(date)
                    .signWith(key)
                    .compact();
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key).
                build().
                parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        Claims claims = Jwts.parser()
                            .verifyWith(key)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();

        return claims;
    }
}
