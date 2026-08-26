package com.project.lovable_clone.dto.subscription;

public record PlanResponse(
    Long id,
    String name,
    Long maxProjects,
    Long maxTokensPerDay,
    Boolean unlimitedAi,
    String price
) {
}
