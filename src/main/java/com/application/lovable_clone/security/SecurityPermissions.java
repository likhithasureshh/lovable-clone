package com.application.lovable_clone.security;

import com.application.lovable_clone.entity.ProjectMember;
import com.application.lovable_clone.enums.ProjectPermissions;
import com.application.lovable_clone.enums.ProjectRole;
import com.application.lovable_clone.repository.ProjectMemberRepository;
import com.application.lovable_clone.security.AuthUtil.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.application.lovable_clone.enums.ProjectPermissions.*;

@Component("security")
@RequiredArgsConstructor
public class SecurityPermissions {
    private final ProjectMemberRepository projectMemberRepository;
    private final AuthUtil authUtil;


    private boolean getPermissions(Long projectId, ProjectPermissions projectPermissions)
    {
        Long userId = authUtil.getCurrentUserId();
        return projectMemberRepository.findRoleByProjectIdAndUserId(projectId,userId)
                .map(role -> role.getPermissions().contains(projectPermissions))
                .orElse(false);
    }
    public boolean canViewProject(Long projectId)
    {
        return getPermissions(projectId,VIEW);
    }

    public boolean canEditProject(Long projectId)
    {
        return getPermissions(projectId,EDIT);
    }

    public boolean canDeleteProject(Long projectId)
    {
        return getPermissions(projectId,DELETE);
    }


    public boolean canViewProjectMembers(Long projectId)
    {
        return getPermissions(projectId,VIEW_MEMBERS);
    }

    public boolean canEditProjectMembers(Long projectId)
    {
        return getPermissions(projectId,EDIT_MEMBERS);
    }

    public boolean canManageProjectMembers(Long projectId)
    {
        return getPermissions(projectId,MANAGE_MEMBERS);
    }

}
