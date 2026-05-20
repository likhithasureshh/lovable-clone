package com.application.lovable_clone.dto.member;

import com.application.lovable_clone.enums.ProjectRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest(
        @NotBlank @Email String username,
        @NotNull ProjectRole role
) {
}
