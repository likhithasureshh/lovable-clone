package com.project.lovable_clone.mapper;

import com.project.lovable_clone.dto.auth.SignupRequest;
import com.project.lovable_clone.dto.auth.UserProfileResponse;
import com.project.lovable_clone.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUserFromSignUpRequest(SignupRequest signupRequest);
    UserProfileResponse toUserProfileResponse(User user);
}
