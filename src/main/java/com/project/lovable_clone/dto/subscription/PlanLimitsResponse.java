package com.project.lovable_clone.dto.subscription;

public record PlanLimitsResponse(
        String planName,
        Integer maxProjects,
        Integer maxTokensPerDay,
        Boolean unlimitedAi


) {
}
