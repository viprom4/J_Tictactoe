package com.example.J_Tictactoe.web.security;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.example.J_Tictactoe.domain.model.Role;

public class JwtAuthentication implements Authentication {
    private UUID id;
    private List<GrantedAuthority> roles;
    private boolean authenticated = true;
    
    public JwtAuthentication(UUID id, List<Role> roles) {
        this.id = id;
        this.roles = roles.stream()
                .map(role -> (GrantedAuthority) () -> role.name())
                .toList();
    }

    @Override
    public UUID getPrincipal() {
        return id;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getName() {
        return id.toString();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
