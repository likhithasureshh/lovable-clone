package com.application.lovable_clone.service.impl;

import com.application.lovable_clone.dto.auth.AuthResponse;
import com.application.lovable_clone.dto.auth.LoginRequest;
import com.application.lovable_clone.dto.auth.SignUpRequest;
import com.application.lovable_clone.entity.User;
import com.application.lovable_clone.errors.BadRequestException;
import com.application.lovable_clone.mapper.UserMapper;
import com.application.lovable_clone.repository.UserRepository;
import com.application.lovable_clone.security.AuthUtil.AuthUtil;
import com.application.lovable_clone.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthUtil authUtil;
    AuthenticationManager authenticationManager;
    @Override
    public AuthResponse signUp(SignUpRequest signUpRequest) {
        userRepository.findByUsername(signUpRequest.username()).ifPresent(user->
        {
            throw new BadRequestException("User is found with username:"+signUpRequest.username());
        });
        User user = userMapper.toUser(signUpRequest);
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        userRepository.save(user);

        String token = authUtil.generateAccessToken(user);

        return new AuthResponse(token,userMapper.fromUserToUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(),loginRequest.password())
        );
        User user = (User) authentication.getPrincipal();
        String token = authUtil.generateAccessToken(user);
        return new AuthResponse(token,userMapper.fromUserToUserProfileResponse(user));

    }
}
