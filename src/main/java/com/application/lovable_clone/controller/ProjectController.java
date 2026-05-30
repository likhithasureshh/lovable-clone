package com.application.lovable_clone.controller;

import com.application.lovable_clone.dto.project.ProjectRequest;
import com.application.lovable_clone.dto.project.ProjectResponse;
import com.application.lovable_clone.dto.project.ProjectSummaryResponse;
import com.application.lovable_clone.service.ProjectService;
import jakarta.validation.Valid;
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

        return ResponseEntity.ok(projectService.getUserProjects());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id)
    {

        return ResponseEntity.ok(projectService.getUserProjectsById(id));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectRequest projectRequest)
    {

        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(projectRequest));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id,@RequestBody @Valid ProjectRequest projectRequest)
    {

        return ResponseEntity.ok(projectService.updateProject(id,projectRequest));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id)
    {

        projectService.softDelete(id);
        return ResponseEntity.noContent().build();

    }

}
