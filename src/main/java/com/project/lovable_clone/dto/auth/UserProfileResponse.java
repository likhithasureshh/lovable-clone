package com.project.lovable_clone.dto.auth;

public record UserProfileResponse (
        Long id,
        String name,
        String username,
        String avatarUrl
){
}
