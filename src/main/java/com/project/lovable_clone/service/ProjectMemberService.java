package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.members.InviteMemberRequest;
import com.project.lovable_clone.dto.members.MemberResponse;
import com.project.lovable_clone.dto.members.UpdateMemberRoleRequest;

public interface ProjectMemberService {
    MemberResponse getProjectMembers(Long projectId, Long userId);

    MemberResponse inviteProjectMember(Long projectId, InviteMemberRequest request, Long userId);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId);

    MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId);
}
