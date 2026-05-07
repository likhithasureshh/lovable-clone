package com.application.lovable_clone.service.impl;

import com.application.lovable_clone.dto.auth.AuthResponse;
import com.application.lovable_clone.dto.auth.LoginRequest;
import com.application.lovable_clone.dto.auth.SignUpRequest;
import com.application.lovable_clone.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponse signUp(SignUpRequest signUpRequest) {
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        return null;
    }
}
