package com.project.lovable_clone.controller;

import com.project.lovable_clone.dto.auth.AuthResponse;
import com.project.lovable_clone.dto.auth.LoginRequest;
import com.project.lovable_clone.dto.auth.SignupRequest;
import com.project.lovable_clone.dto.auth.UserProfileResponse;
import com.project.lovable_clone.service.AuthService;
import com.project.lovable_clone.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;


    @PostMapping(path = "/signup")
    public ResponseEntity<AuthResponse> singup(@RequestBody @Valid SignupRequest request)
    {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request)
    {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping(path = "/me")
    public ResponseEntity<UserProfileResponse> getProfile()
    {
        Long userId = 1L;
        return ResponseEntity.ok(userService.getProfile(userId));
    }
}
