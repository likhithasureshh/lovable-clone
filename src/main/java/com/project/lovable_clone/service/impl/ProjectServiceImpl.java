package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.project.ProjectRequest;
import com.project.lovable_clone.dto.project.ProjectResponse;
import com.project.lovable_clone.dto.project.ProjectSummaryResponse;
import com.project.lovable_clone.entity.Project;
import com.project.lovable_clone.entity.User;
import com.project.lovable_clone.mapper.ProjectMapper;
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

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User owner = userRepository.findById(userId).orElseThrow();
        Project project = Project.builder()
                .name(request.name())
                .owner(owner)
                .isPublic(false)
                .build();
        project = projectRepository.save(project);
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
        if(!project.getOwner().getId().equals(id))
        {
            throw new RuntimeException("You are not allowed to update");
        }
        project.setName(request.name());
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long id, Long userId)
    {
       Project project = getAccessibleUserProjectById(id, userId);
       if(!project.getOwner().getId().equals(userId))
       {
           throw new RuntimeException("You are not allowed to delete");
       }
       project.setDeletedAt(Instant.now());
       projectRepository.save(project);
    }

    public Project getAccessibleUserProjectById(Long projectId,Long userId)
    {
        return projectRepository.findAccessibleUserProjectById(projectId,userId).orElseThrow();
    }
}
