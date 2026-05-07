package com.application.lovable_clone.service.impl;

import com.application.lovable_clone.dto.member.InviteMemberRequest;
import com.application.lovable_clone.dto.member.MemberResponse;
import com.application.lovable_clone.dto.member.UpdateMemberRoleRequest;
import com.application.lovable_clone.service.ProjectMemberService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {
    @Override
    public List<MemberResponse> getAllProjectMembers(Long projectId, Long userId) {
        return List.of();
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest inviteMemberRequest, Long userId) {
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
