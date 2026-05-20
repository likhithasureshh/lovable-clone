package com.application.lovable_clone.dto.member;

import com.application.lovable_clone.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long id,
        String name,
        String username,
        ProjectRole projectRole,
        Instant invitedAt
) {
}
