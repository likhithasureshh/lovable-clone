package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.auth.AuthResponse;
import com.project.lovable_clone.dto.auth.LoginRequest;
import com.project.lovable_clone.dto.auth.SignupRequest;
import com.project.lovable_clone.entity.User;
import com.project.lovable_clone.errors.BadRequestException;
import com.project.lovable_clone.mapper.UserMapper;
import com.project.lovable_clone.repository.UserRepository;
import com.project.lovable_clone.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    @Override
    public AuthResponse signup(SignupRequest request) {
        userRepository.findByUsername(request.username())
                .ifPresent(user -> {
                    throw new BadRequestException("User already exists!You cannot signup!");
                });
        User user = userMapper.toUserFromSignUpRequest(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new AuthResponse("dummy",userMapper.toUserProfileResponse(user));


    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
