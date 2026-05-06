package com.application.lovable_clone.dto.project;

import com.application.lovable_clone.dto.auth.UserProfileResponse;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String name,
        Instant updatedAt,
        Instant createdAt,
        UserProfileResponse owner
) {
}
