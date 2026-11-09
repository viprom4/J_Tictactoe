package com.example.J_Tictactoe.web.security;

import java.util.List;
import java.util.UUID;

import com.example.J_Tictactoe.domain.model.Role;

import io.jsonwebtoken.Claims;

public class JwtUtil {
    public static JwtAuthentication createJwtAuthentication(Claims claims) {
        UUID userId = UUID.fromString(claims.getSubject());

        List<String> roleStrings = claims.get("roles", List.class);
        List<Role> roles = roleStrings.stream()
                                        .map(Role::valueOf)
                                        .toList();
        
        return new JwtAuthentication(userId, roles);
    }
}
