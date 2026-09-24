package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.project.ProjectRequest;
import com.project.lovable_clone.dto.project.ProjectResponse;
import com.project.lovable_clone.dto.project.ProjectSummaryResponse;
import com.project.lovable_clone.entity.Project;
import com.project.lovable_clone.entity.ProjectMember;
import com.project.lovable_clone.entity.ProjectMemberId;
import com.project.lovable_clone.entity.User;
import com.project.lovable_clone.enums.ProjectRole;
import com.project.lovable_clone.errors.BadRequestException;
import com.project.lovable_clone.errors.ResourceNotFoundException;
import com.project.lovable_clone.mapper.ProjectMapper;
import com.project.lovable_clone.repository.ProjectMemberRepository;
import com.project.lovable_clone.repository.ProjectRepository;
import com.project.lovable_clone.repository.UserRepository;
import com.project.lovable_clone.security.AuthUtil;
import com.project.lovable_clone.service.ProjectService;
import com.project.lovable_clone.service.ProjectTemplateService;
import com.project.lovable_clone.service.SubscriptionService;
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
    AuthUtil authUtil;
    SubscriptionService subscriptionService;
    ProjectTemplateService projectTemplateService;

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        if(!subscriptionService.canCreateNewProjects())
        {
            throw new BadRequestException("You cannot create the project with this Plan,Upgrade Now!");
        }
        Long userId = authUtil.getCurrentUserId();
        //User owner = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user",userId.toString()));
        User owner = userRepository.getReferenceById(userId);
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
        projectTemplateService.initializeProjectFromTemplate(project.getId());
        return projectMapper.toProjectResponse(project);


    }
    @Override
    public List<ProjectSummaryResponse> getUserProjects() {
        Long userId = authUtil.getCurrentUserId();
        List<Project> projectList = projectRepository.findAllAccessibleByUser(userId);
        return projectMapper.toProjectSummaryResponse(projectList);
    }

    @Override
    public ProjectResponse getUserProjectsById(Long id) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleUserProjectById(id, userId);
        return projectMapper.toProjectResponse(project);
    }



    @Override
    public ProjectResponse updateProjectById(Long id, ProjectRequest request) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleUserProjectById(id,userId);
        project.setName(request.name());
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long id)
    {
        Long userId = authUtil.getCurrentUserId();
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
