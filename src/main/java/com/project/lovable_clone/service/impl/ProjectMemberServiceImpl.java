package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.members.InviteMemberRequest;
import com.project.lovable_clone.dto.members.MemberResponse;
import com.project.lovable_clone.dto.members.UpdateMemberRoleRequest;
import com.project.lovable_clone.entity.Project;
import com.project.lovable_clone.entity.ProjectMember;
import com.project.lovable_clone.entity.ProjectMemberId;
import com.project.lovable_clone.entity.User;
import com.project.lovable_clone.errors.ResourceNotFoundException;
import com.project.lovable_clone.mapper.ProjectMapper;
import com.project.lovable_clone.mapper.ProjectMemberMapper;
import com.project.lovable_clone.repository.ProjectMemberRepository;
import com.project.lovable_clone.repository.ProjectRepository;
import com.project.lovable_clone.repository.UserRepository;
import com.project.lovable_clone.service.ProjectMemberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {
    ProjectMemberRepository projectMemberRepository;
    ProjectRepository projectRepository;
    ProjectMemberMapper projectMemberMapper;
    UserRepository userRepository;
    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {
        Project project = getAccessibleUserProjectById(projectId,userId);

        return projectMemberRepository.findByIdProjectId(projectId).stream()
                        .map(projectMemberMapper::toMemberResponseFromProjectMember)
                        .collect(Collectors.toList());
    }

    @Override
    public MemberResponse inviteProjectMember(Long projectId, InviteMemberRequest request, Long userId)
    {
        Project project = getAccessibleUserProjectById(projectId,userId);
        User invitee = userRepository.findByUsername(request.username())
                .orElseThrow(()-> new ResourceNotFoundException("user",userId.toString()));
        if(invitee.getId().equals(userId))
        {
            throw new RuntimeException("You cannot invite yourself!");
        }
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId,invitee.getId());
        if(projectMemberRepository.existsById(projectMemberId))
        {
            throw new RuntimeException("You are already a Project Member!");
        }
        ProjectMember projectMember = ProjectMember.builder()
                .id(projectMemberId)
                .projectRole(request.projectRole())
                .invitedAt(Instant.now())
                .user(invitee)
                .project(project)
                .build();
        projectMemberRepository.save(projectMember);
        return projectMemberMapper.toMemberResponseFromProjectMember(projectMember);
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId)
    {
        Project project = getAccessibleUserProjectById(projectId,userId);
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId,memberId);
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId).orElseThrow();
        projectMember.setProjectRole(request.projectRole());
        projectMember=projectMemberRepository.save(projectMember);

        return projectMemberMapper.toMemberResponseFromProjectMember(projectMember);
    }

    @Override
    public void removeProjectMember(Long projectId, Long memberId, Long userId) {
        Project project = getAccessibleUserProjectById(projectId,userId);
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId,memberId);
        projectMemberRepository.deleteById(projectMemberId);
    }

    public Project getAccessibleUserProjectById(Long projectId, Long userId)
    {
        return projectRepository.findAccessibleUserProjectById(projectId,userId)
                .orElseThrow(()-> new ResourceNotFoundException("project",projectId.toString()));
    }
}
