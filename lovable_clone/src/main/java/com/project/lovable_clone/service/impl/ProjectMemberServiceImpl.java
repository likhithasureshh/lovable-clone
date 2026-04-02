package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.member.InviteMemberRequest;
import com.project.lovable_clone.dto.member.MemberResponse;
import com.project.lovable_clone.dto.member.UpdateMemberRoleRequest;
import com.project.lovable_clone.entity.Project;
import com.project.lovable_clone.entity.ProjectMember;
import com.project.lovable_clone.entity.ProjectMemberId;
import com.project.lovable_clone.entity.User;
import com.project.lovable_clone.mapper.ProjectMemberMapper;
import com.project.lovable_clone.repository.ProjectMemberRepository;
import com.project.lovable_clone.repository.ProjectRepository;
import com.project.lovable_clone.repository.UserRepository;
import com.project.lovable_clone.service.ProjectMemberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {
    ProjectRepository projectRepository;
    ProjectMemberMapper projectMemberMapper;
    ProjectMemberRepository projectMemberRepository;
    UserRepository userRepository;
    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId)
    {
        Project project = getFindAccessibleProjectById(projectId,userId);
        List<MemberResponse> memberResponseList = new ArrayList<>();
        memberResponseList.add(projectMemberMapper.toMemberResponseFromUser(project.getOwner()));
        memberResponseList.addAll(
                projectMemberRepository.findByIdProjectId(projectId)
                        .stream()
                        .map(projectMember -> projectMemberMapper.toMemberResponseFromProjectMember(projectMember))
                        .toList()
        );
       return memberResponseList;
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId)
    {
      Project project = getFindAccessibleProjectById(projectId,userId);
      User invitee = userRepository.findByEmail(request.email()).orElseThrow();
      ProjectMemberId projectMemberId = new ProjectMemberId(invitee.getId(),projectId);
        if(!project.getOwner().getId().equals(userId))
        {
            throw new RuntimeException("You are not allowed to invitee");
        }
      if(invitee.getId().equals(userId))
      {
          throw new RuntimeException("You cannot invite yourself!");
      }
      if(projectMemberRepository.existsById(projectMemberId))
      {
          throw new RuntimeException("You are already a member!");
      }
        ProjectMember projectMember = ProjectMember.builder()
                .id(projectMemberId)
                .role(request.role())
                .invitedAt(Instant.now())
                .user(invitee)
                .project(project)
                .build();
      projectMemberRepository.save(projectMember);
      return projectMemberMapper.toMemberResponseFromProjectMember(projectMember);

    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId , UpdateMemberRoleRequest request, Long userId)
    {
        Project project = getFindAccessibleProjectById(projectId,userId);
        if(!project.getOwner().getId().equals(userId))
        {
            throw new RuntimeException("You are not allowed to update the member role!");
        }
        ProjectMemberId projectMemberId = new ProjectMemberId(memberId,projectId);
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId).orElseThrow();
        projectMember.setRole(request.role());
        projectMemberRepository.save(projectMember);
        return projectMemberMapper.toMemberResponseFromProjectMember(projectMember);


    }

    @Override
    public void deleteProjectMember(Long projectId, Long memberId,Long userId)
    {
        Project project = getFindAccessibleProjectById(projectId,userId);
        if(!project.getOwner().getId().equals(userId))
        {
            throw new RuntimeException("You are not allowed to update the member role!");
        }
        ProjectMemberId projectMemberId = new ProjectMemberId(memberId,projectId);
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId).orElseThrow();
        projectMemberRepository.deleteById(projectMemberId);

    }

    public Project getFindAccessibleProjectById(Long projectId, Long userId)
    {
        return projectRepository.findAccessibleProjectById(projectId,userId).orElseThrow();
    }
}
