package com.application.lovable_clone.dto.member;

import com.application.lovable_clone.enums.ProjectRole;

public record InviteMemberRequest(
        String email,
        ProjectRole role
) {
}
