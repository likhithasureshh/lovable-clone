package com.project.lovable_clone.dto.members;

import com.project.lovable_clone.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(
       @NotNull ProjectRole projectRole
) {
}
