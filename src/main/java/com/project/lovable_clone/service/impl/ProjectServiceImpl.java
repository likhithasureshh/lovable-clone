package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.project.ProjectRequest;
import com.project.lovable_clone.dto.project.ProjectResponse;
import com.project.lovable_clone.dto.project.ProjectSummaryResponse;
import com.project.lovable_clone.entity.Project;
import com.project.lovable_clone.entity.ProjectMember;
import com.project.lovable_clone.entity.ProjectMemberId;
import com.project.lovable_clone.entity.User;
import com.project.lovable_clone.enums.ProjectRole;
import com.project.lovable_clone.errors.ResourceNotFoundException;
import com.project.lovable_clone.mapper.ProjectMapper;
import com.project.lovable_clone.repository.ProjectMemberRepository;
import com.project.lovable_clone.repository.ProjectRepository;
import com.project.lovable_clone.repository.UserRepository;
import com.project.lovable_clone.service.ProjectService;
import com.project.lovable_clone.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Transactional
public class ProjectServiceImpl implements ProjectService {
    UserRepository userRepository;
    ProjectRepository projectRepository;
    ProjectMapper projectMapper;
    ProjectMemberRepository projectMemberRepository;

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User owner = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user",userId.toString()));
        Project project = Project.builder()
                .name(request.name())
                .isPublic(false)
                .build();
        project = projectRepository.save(project);

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(),owner.getId());
        ProjectMember projectMember = ProjectMember
                .builder()
                .id(projectMemberId)
                .user(owner)
                .projectRole(ProjectRole.OWNER)
                .project(project)
                .invitedAt(Instant.now())
                .acceptedAt(Instant.now())
                .build();
        projectMemberRepository.save(projectMember);
        return projectMapper.toProjectResponse(project);


    }
    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {
        List<Project> projectList = projectRepository.findAllAccessibleByUser(userId);
        return projectMapper.toProjectSummaryResponse(projectList);
    }

    @Override
    public ProjectResponse getUserProjectsById(Long id, Long userId) {
        Project project = getAccessibleUserProjectById(id, userId);
        return projectMapper.toProjectResponse(project);
    }



    @Override
    public ProjectResponse updateProjectById(Long id, ProjectRequest request, Long userId) {
        Project project = getAccessibleUserProjectById(id,userId);
        project.setName(request.name());
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long id, Long userId)
    {
       Project project = getAccessibleUserProjectById(id, userId);
       project.setDeletedAt(Instant.now());
       projectRepository.save(project);
    }

    public Project getAccessibleUserProjectById(Long projectId,Long userId)
    {
        return projectRepository.findAccessibleUserProjectById(projectId,userId)
                .orElseThrow(()-> new ResourceNotFoundException("project",projectId.toString()));
    }
}
