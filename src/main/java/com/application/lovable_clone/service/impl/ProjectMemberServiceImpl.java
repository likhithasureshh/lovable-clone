package com.application.lovable_clone.service.impl;

import com.application.lovable_clone.dto.member.InviteMemberRequest;
import com.application.lovable_clone.dto.member.MemberResponse;
import com.application.lovable_clone.dto.member.UpdateMemberRoleRequest;
import com.application.lovable_clone.entity.Project;
import com.application.lovable_clone.entity.ProjectMember;
import com.application.lovable_clone.entity.ProjectMemberId;
import com.application.lovable_clone.entity.User;
import com.application.lovable_clone.mapper.ProjectMemberMapper;
import com.application.lovable_clone.repository.ProjectMemberRepository;
import com.application.lovable_clone.repository.ProjectRepository;
import com.application.lovable_clone.repository.UserRepository;
import com.application.lovable_clone.service.ProjectMemberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ProjectMemberServiceImpl implements ProjectMemberService {

    ProjectRepository projectRepository;
    ProjectMemberRepository projectMemberRepository;
    ProjectMemberMapper projectMemberMapper;
    UserRepository userRepository;

    @Override
    public List<MemberResponse> getAllProjectMembers(Long projectId, Long userId) {

        Project project = getAccessibleByProjectId(projectId,userId);
        return projectMemberRepository.findByIdProjectId(projectId)
                .stream()
                .map(projectMember -> projectMemberMapper.toMemberResponseFromInvite(projectMember))
                .toList();
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest inviteMemberRequest, Long userId) {
        Project project = getAccessibleByProjectId(projectId,userId);

        User invitee = userRepository.findByUsername(inviteMemberRequest.username()).orElseThrow();

        if(invitee.getId().equals(userId))
        {
            throw new RuntimeException("You can not invite yourself");
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId,invitee.getId());
        if(projectMemberRepository.existsById(projectMemberId))
        {
            throw new RuntimeException("You cannot invite youself again!..");
        }

        ProjectMember projectMember = ProjectMember.builder()
                .id(projectMemberId)
                .project(project)
                .user(invitee)
                .invitedAt(Instant.now())
                .projectRole(inviteMemberRequest.role())
                .build();
        projectMember=projectMemberRepository.save(projectMember);
        return projectMemberMapper.toMemberResponseFromInvite(projectMember);
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId) {
        Project project = getAccessibleByProjectId(projectId,userId);
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId,memberId);
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId).orElseThrow();
        projectMember.setProjectRole(request.role());
        projectMember=projectMemberRepository.save(projectMember);
        return projectMemberMapper.toMemberResponseFromInvite(projectMember);
    }

    @Override
    public void removeProjectMember(Long projectId, Long memberId, Long userId) {
        Project project = getAccessibleByProjectId(projectId,userId);
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId,memberId);
        if(!projectMemberRepository.existsById(projectMemberId))
        {
            throw new RuntimeException("Project Member doesnt exits");
        }
        projectMemberRepository.deleteById(projectMemberId);
    }

    public Project getAccessibleByProjectId(Long projectId, Long userId)
    {
        return projectRepository.findAccessibleProjectById(projectId,userId).orElseThrow();
    }
}
