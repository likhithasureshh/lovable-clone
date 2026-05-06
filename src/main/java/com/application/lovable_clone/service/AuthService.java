package com.application.lovable_clone.service;

import com.application.lovable_clone.dto.auth.AuthResponse;
import com.application.lovable_clone.dto.auth.LoginRequest;
import com.application.lovable_clone.dto.auth.SignUpRequest;

public interface AuthService {
    AuthResponse signUp(SignUpRequest signUpRequest);

    AuthResponse login(LoginRequest loginRequest);
}
