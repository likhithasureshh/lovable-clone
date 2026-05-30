package com.application.lovable_clone.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record JwtPrincipal(
        String username,
        Long userId,
        List<GrantedAuthority> authorities
) {
}
