package com.application.lovable_clone.controller;

import com.application.lovable_clone.dto.auth.AuthResponse;
import com.application.lovable_clone.dto.auth.LoginRequest;
import com.application.lovable_clone.dto.auth.SignUpRequest;
import com.application.lovable_clone.dto.auth.UserProfileResponse;
import com.application.lovable_clone.service.AuthService;
import com.application.lovable_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;


    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> signUp(@RequestBody SignUpRequest signUpRequest)
    {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest)
    {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @GetMapping(path = "/me")
    public ResponseEntity<UserProfileResponse> getMyProfile()
    {
        Long userId = 1L;
        return ResponseEntity.ok(userService.getMyProfile(userId));
    }

}
