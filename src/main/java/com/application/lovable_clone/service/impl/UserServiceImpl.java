package com.application.lovable_clone.service.impl;

import com.application.lovable_clone.dto.auth.UserProfileResponse;
import com.application.lovable_clone.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserProfileResponse getMyProfile(Long userId) {
        return null;
    }
}
