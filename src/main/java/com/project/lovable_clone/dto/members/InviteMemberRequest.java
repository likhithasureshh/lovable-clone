package com.project.lovable_clone.dto.members;

import com.project.lovable_clone.enums.ProjectRole;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest(
        @Email @NotBlank String username,
        @NotNull ProjectRole projectRole
) {
}

