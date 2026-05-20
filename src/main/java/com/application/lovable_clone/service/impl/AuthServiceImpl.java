package com.application.lovable_clone.service.impl;

import com.application.lovable_clone.dto.auth.AuthResponse;
import com.application.lovable_clone.dto.auth.LoginRequest;
import com.application.lovable_clone.dto.auth.SignUpRequest;
import com.application.lovable_clone.entity.User;
import com.application.lovable_clone.errors.BadRequestException;
import com.application.lovable_clone.mapper.UserMapper;
import com.application.lovable_clone.repository.UserRepository;
import com.application.lovable_clone.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    @Override
    public AuthResponse signUp(SignUpRequest signUpRequest) {
        userRepository.findByUsername(signUpRequest.username()).ifPresent(user->
        {
            throw new BadRequestException("User is not found with username:"+signUpRequest.username());
        });
        User user = userMapper.toUser(signUpRequest);
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        userRepository.save(user);

        return new AuthResponse("dummy",userMapper.fromUserToUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        return null;
    }
}
