package com.application.lovable_clone.service;

import com.application.lovable_clone.dto.member.InviteMemberRequest;
import com.application.lovable_clone.dto.member.MemberResponse;
import com.application.lovable_clone.dto.member.UpdateMemberRoleRequest;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getAllProjectMembers(Long projectId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest inviteMemberRequest);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request);

    void removeProjectMember(Long projectId, Long memberId);
}
