package com.application.lovable_clone.service.impl;

import com.application.lovable_clone.dto.project.ProjectRequest;
import com.application.lovable_clone.dto.project.ProjectResponse;
import com.application.lovable_clone.dto.project.ProjectSummaryResponse;
import com.application.lovable_clone.entity.Project;
import com.application.lovable_clone.entity.ProjectMember;
import com.application.lovable_clone.entity.ProjectMemberId;
import com.application.lovable_clone.entity.User;
import com.application.lovable_clone.enums.ProjectRole;
import com.application.lovable_clone.errors.ResourceNotFoundException;
import com.application.lovable_clone.mapper.ProjectMapper;
import com.application.lovable_clone.repository.ProjectMemberRepository;
import com.application.lovable_clone.repository.ProjectRepository;
import com.application.lovable_clone.repository.UserRepository;
import com.application.lovable_clone.security.AuthUtil.AuthUtil;
import com.application.lovable_clone.service.ProjectService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;
    ProjectMemberRepository projectMemberRepository;
    AuthUtil authUtil;

    @Override
    public List<ProjectSummaryResponse> getUserProjects() {
        Long userId = authUtil.getCurrentUserId();
       List<Project> projects = projectRepository.findAllAccessibleByUser(userId);
       return projectMapper.toListOfProjectSummaryResponse(projects);
    }

    @Override
    public ProjectResponse getUserProjectsById(Long id) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleByProjectId(id,userId);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest projectRequest) {
        Long userId = authUtil.getCurrentUserId();
        User owner = userRepository.findById(userId).orElseThrow();
        Project project = Project.builder()
                .name(projectRequest.name())
                .isPublic(false)
                .build();

        project = projectRepository.save(project);

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(),userId);
        ProjectMember projectMember = ProjectMember.builder()
                .id(projectMemberId)
                .projectRole(ProjectRole.OWNER)
                .invitedAt(Instant.now())
                .acceptedAt(Instant.now())
                .project(project)
                .user(owner)
                .build();

        projectMemberRepository.save(projectMember);

        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest projectRequest) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleByProjectId(id,userId);

        project.setName(projectRequest.name());
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long id) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleByProjectId(id,userId);

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }

    public Project getAccessibleByProjectId(Long projectId,Long userId)
    {
        return projectRepository.findAccessibleProjectById(projectId,userId)
                .orElseThrow(()-> new ResourceNotFoundException("Project",projectId.toString()));
    }
}
