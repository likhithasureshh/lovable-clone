package com.application.lovable_clone.security;

import com.application.lovable_clone.security.AuthUtil.AuthUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final AuthUtil authUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

       try {
           log.info("request:{}", request.getRequestURI());
           String requestToken = request.getHeader("Authorization");
           if (requestToken == null || !requestToken.startsWith("Bearer")) {
               filterChain.doFilter(request, response);
               return;
           }
           String jwtToken = requestToken.split("Bearer ")[1];
           JwtPrincipal user = authUtil.verifyAccessToken(jwtToken);
           if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
               UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.authorities());
               SecurityContextHolder.getContext().setAuthentication(authenticationToken);
           }
           filterChain.doFilter(request, response);
       }
       catch (Exception e)
       {
            handlerExceptionResolver.resolveException(request,response,null,e);
       }
    }
}
