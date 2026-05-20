package com.application.lovable_clone.mapper;

import com.application.lovable_clone.dto.auth.AuthResponse;
import com.application.lovable_clone.dto.auth.SignUpRequest;
import com.application.lovable_clone.dto.auth.UserProfileResponse;
import com.application.lovable_clone.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(SignUpRequest signUpRequest);
    UserProfileResponse fromUserToUserProfileResponse(User user);
}
