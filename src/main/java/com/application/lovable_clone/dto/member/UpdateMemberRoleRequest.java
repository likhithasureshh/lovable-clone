package com.application.lovable_clone.dto.member;

import com.application.lovable_clone.enums.ProjectRole;

public record UpdateMemberRoleRequest(
        ProjectRole role
) {
}
