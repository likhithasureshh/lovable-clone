package com.project.lovable_clone.dto.members;

import com.project.lovable_clone.enums.ProjectRole;

public record MemberResponse(
        Long id,
        String name,
        String email,
        String avatarUrl,
        String invitedAt,
        ProjectRole projectRole
) {
}
