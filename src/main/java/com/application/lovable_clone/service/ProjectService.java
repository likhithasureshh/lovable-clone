package com.application.lovable_clone.service;

import com.application.lovable_clone.dto.project.ProjectRequest;
import com.application.lovable_clone.dto.project.ProjectResponse;
import com.application.lovable_clone.dto.project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResponse> getUserProjects();

    ProjectResponse getUserProjectsById(Long id);

    ProjectResponse createProject(ProjectRequest projectRequest);

    ProjectResponse updateProject(Long id, ProjectRequest projectRequest);

    void softDelete(Long id);
}
