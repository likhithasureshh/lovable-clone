package com.project.lovable_clone.dto.members;

import com.project.lovable_clone.enums.ProjectRole;

public record UpdateMemberRoleRequest(
        ProjectRole projectRole
) {
}
