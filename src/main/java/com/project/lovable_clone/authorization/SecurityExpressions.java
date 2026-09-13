package com.project.lovable_clone.authorization;

import com.project.lovable_clone.enums.ProjectPermissions;
import com.project.lovable_clone.enums.ProjectRole;
import com.project.lovable_clone.repository.ProjectMemberRepository;
import com.project.lovable_clone.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.project.lovable_clone.enums.ProjectPermissions.*;

@Component("security")
@RequiredArgsConstructor
public class SecurityExpressions {

    private final ProjectMemberRepository projectMemberRepository;
    private final AuthUtil authUtil;

    public boolean handlePermissions(Long projectId,ProjectPermissions projectPermissions)
    {
        Long userId = authUtil.getCurrentUserId();
        return projectMemberRepository.findRoleByProjectIdAndUserId(projectId,userId)
                .map(role-> role.getPermissionsSet().contains(projectPermissions))
                .orElse(false);
    }

    public boolean canViewProject(Long projectId)
    {
        return handlePermissions(projectId,VIEW);
    }
    public boolean canEditProject(Long projectId)
    {
        return handlePermissions(projectId,EDIT);
    }
    public boolean canDeleteProject(Long projectId)
    {
        return handlePermissions(projectId,DELETE);
    }

    public boolean canViewMembers(Long projectId)
    {
        return handlePermissions(projectId,VIEW);
    }

    public boolean canManageMembers(Long projectId)
    {
        return handlePermissions(projectId,MANAGE_MEMBERS);
    }

}
