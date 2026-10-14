package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.deploy.DeployResponse;

public interface DeploymentService {
    public DeployResponse deploy(Long projectId);
}
