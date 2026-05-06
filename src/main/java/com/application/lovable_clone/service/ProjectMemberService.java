package com.application.lovable_clone.service;

import com.application.lovable_clone.dto.member.InviteMemberRequest;
import com.application.lovable_clone.dto.member.MemberResponse;
import com.application.lovable_clone.dto.member.UpdateMemberRoleRequest;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getAllProjectMembers(Long projectId, Long userId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest inviteMemberRequest, Long userId);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId);

    MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId);
}
