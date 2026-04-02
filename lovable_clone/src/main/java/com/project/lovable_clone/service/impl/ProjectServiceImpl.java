package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.project.ProjectRequest;
import com.project.lovable_clone.dto.project.ProjectResponse;
import com.project.lovable_clone.dto.project.ProjectSummaryResponse;
import com.project.lovable_clone.entity.Project;
import com.project.lovable_clone.mapper.ProjectMapper;
import com.project.lovable_clone.repository.ProjectRepository;
import com.project.lovable_clone.repository.UserRepository;
import com.project.lovable_clone.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import com.project.lovable_clone.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Transactional
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    ProjectMapper projectMapper;
    UserRepository userRepository;
    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId)
    {
//        return projectRepository.findAllAccessibleByUser(userId)
//                .stream()
//                .map(project -> projectMapper.toProjectSummaryResponse(project))
//                .collect(Collectors.toList());
        return projectMapper.toListProjectSummaryResponse(projectRepository.findAllAccessibleByUser(userId));
    }

    @Override
    public ProjectResponse getUserProjectById(Long id, Long userId)
    {
        Project project = getFindAccessibleByProjectId(id,userId);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest projectRequest, Long userId)
    {
        User user = (User) userRepository.findById(userId).orElseThrow();
        Project project = Project.builder()
                .name(projectRequest.name())
                .isPublic(false)
                .owner(user)
                .build();
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);

    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest projectRequest, Long userId)
    {
        Project project = getFindAccessibleByProjectId(id,userId);
        if(!project.getOwner().getId().equals(userId))
        {
            throw new RuntimeException("You are not allowed to update the name");
        }
        project.setName(projectRequest.name());
        projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long id, Long userId)
    {
      Project project = getFindAccessibleByProjectId(id,userId);
      if(!project.getOwner().getId().equals(userId))
      {
          throw new RuntimeException("You are not allowed to delete the project");
      }
      project.setDeletedAt(Instant.now());
      projectRepository.save(project);
    }

    public Project getFindAccessibleByProjectId(Long projectId,Long userId)
    {
        return projectRepository.findAccessibleProjectById(projectId,userId).orElseThrow();
    }
}
