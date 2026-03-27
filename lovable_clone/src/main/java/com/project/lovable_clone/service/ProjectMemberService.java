package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.member.InviteMemberRequest;
import com.project.lovable_clone.dto.member.MemberResponse;
import com.project.lovable_clone.dto.member.UpdateMemberRoleRequest;
import org.springframework.stereotype.Service;

@Service
public interface ProjectMemberService {
    MemberResponse getProjectMembers(Long projectId, Long userId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId);

    MemberResponse updateMemberRole(Long projectId, UpdateMemberRoleRequest request, Long userId);

    MemberResponse deleteProjectMember(Long projectId, Long memberId);
}
