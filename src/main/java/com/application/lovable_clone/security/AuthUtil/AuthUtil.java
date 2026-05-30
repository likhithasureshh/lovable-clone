package com.application.lovable_clone.security.AuthUtil;

import com.application.lovable_clone.entity.User;
import com.application.lovable_clone.security.JwtPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;


@Service

public class AuthUtil {
    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    public SecretKey getSecretKey()
    {
       return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user)
    {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId",user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10))
                .signWith(getSecretKey())
                .compact();
    }

    public JwtPrincipal verifyAccessToken(String token)
    {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String username = claims.getSubject();
        Long userId = claims.get("userId", Long.class);
        return new JwtPrincipal(username,userId,new ArrayList<>());

    }

    public Long getCurrentUserId()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !(authentication.getPrincipal() instanceof JwtPrincipal))
        {
            throw new   AuthenticationCredentialsNotFoundException("JWT NOT FOUND");
        }
        return ((JwtPrincipal) authentication.getPrincipal()).userId();
    }

}
