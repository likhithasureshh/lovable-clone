package com.project.lovable_clone.controller;

import com.project.lovable_clone.dto.project.ProjectRequest;
import com.project.lovable_clone.dto.project.ProjectResponse;
import com.project.lovable_clone.dto.project.ProjectSummaryResponse;
import com.project.lovable_clone.service.ProjectService;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok(projectService.getUserProjectsById(id,userId));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest request)
    {
        Long userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request,userId));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<ProjectResponse> updateProjectById(@PathVariable Long id,
                                                             @RequestBody ProjectRequest request)
    {
        Long userId = 1L;
        return ResponseEntity.ok(projectService.updateProjectById(id,request,userId));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable Long id)
    {
        Long userId = 1L;
        projectService.softDelete(id,userId);
        return ResponseEntity.noContent().build();
    }
}
