package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.project.ProjectRequest;
import com.project.lovable_clone.dto.project.ProjectResponse;
import com.project.lovable_clone.dto.project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResponse> getUserProjects();

    ProjectResponse getUserProjectsById(Long id);

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProjectById(Long id, ProjectRequest request);


    void softDelete(Long id);
}
