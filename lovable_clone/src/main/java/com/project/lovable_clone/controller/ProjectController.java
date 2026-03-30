package com.project.lovable_clone.controller;


import com.project.lovable_clone.dto.project.ProjectRequest;
import com.project.lovable_clone.dto.project.ProjectResponse;
import com.project.lovable_clone.dto.project.ProjectSummaryResponse;
import com.project.lovable_clone.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    @GetMapping
    public ResponseEntity<List<ProjectSummaryResponse>> getMyProjects()
    {
        Long userId = 1L;
        return ResponseEntity.ok(projectService.getUserProjects(userId));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id)
    {
        Long userId = 1L;
        return ResponseEntity.ok(projectService.getUserProjectById(id,userId));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest projectRequest)
    {
        Long userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(projectRequest,userId));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id,@RequestBody ProjectRequest projectRequest)
    {
        Long userId = 1L;
        return ResponseEntity.ok(projectService.updateProject(id,projectRequest,userId));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id)
    {
        Long userId = 1L;
        projectService.softDelete(id,userId);
        return ResponseEntity.noContent().build();
    }

}
