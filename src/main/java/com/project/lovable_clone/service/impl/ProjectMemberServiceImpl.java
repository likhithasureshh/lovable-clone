package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.members.InviteMemberRequest;
import com.project.lovable_clone.dto.members.MemberResponse;
import com.project.lovable_clone.dto.members.UpdateMemberRoleRequest;
import com.project.lovable_clone.service.ProjectMemberService;
import org.springframework.stereotype.Service;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {
    @Override
    public MemberResponse getProjectMembers(Long projectId, Long userId) {
        return null;
    }

    @Override
    public MemberResponse inviteProjectMember(Long projectId, InviteMemberRequest request, Long userId) {
        return null;
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId) {
        return null;
    }

    @Override
    public MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId) {
        return null;
    }
}
