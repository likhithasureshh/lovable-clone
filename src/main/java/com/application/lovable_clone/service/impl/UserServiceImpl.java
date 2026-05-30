package com.application.lovable_clone.service.impl;

import com.application.lovable_clone.dto.auth.UserProfileResponse;
import com.application.lovable_clone.errors.ResourceNotFoundException;
import com.application.lovable_clone.repository.UserRepository;
import com.application.lovable_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserProfileResponse getMyProfile(Long userId) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("user",username));
    }
}


