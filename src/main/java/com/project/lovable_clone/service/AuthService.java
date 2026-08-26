package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.auth.AuthResponse;
import com.project.lovable_clone.dto.auth.LoginRequest;
import com.project.lovable_clone.dto.auth.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
