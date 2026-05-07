package com.application.lovable_clone.service.impl;

import com.application.lovable_clone.dto.project.ProjectRequest;
import com.application.lovable_clone.dto.project.ProjectResponse;
import com.application.lovable_clone.dto.project.ProjectSummaryResponse;
import com.application.lovable_clone.entity.Project;
import com.application.lovable_clone.entity.User;
import com.application.lovable_clone.mapper.ProjectMapper;
import com.application.lovable_clone.repository.ProjectRepository;
import com.application.lovable_clone.repository.UserRepository;
import com.application.lovable_clone.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {
       List<Project> projects = projectRepository.findAllAccessibleByUser(userId);
       return projectMapper.toListOfProjectSummaryResponse(projects);
    }

    @Override
    public ProjectResponse getUserProjectsById(Long id, Long userId) {
        return null;
    }

    @Override
    public ProjectResponse createProject(ProjectRequest projectRequest, Long userId) {

        User owner = userRepository.findById(userId).orElseThrow();
        Project project = Project.builder()
                .name(projectRequest.name())
                .isPublic(false)
                .owner(owner)
                .build();

        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest projectRequest, Long userId) {
        return null;
    }

    @Override
    public void softDelete(Long id, Long userId) {

    }
}
