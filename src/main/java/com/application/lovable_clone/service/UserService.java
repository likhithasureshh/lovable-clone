package com.application.lovable_clone.service;

import com.application.lovable_clone.dto.auth.UserProfileResponse;

public interface UserService {
    UserProfileResponse getMyProfile(Long userId);
}
