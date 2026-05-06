package com.application.lovable_clone.dto.subscription;

public record PlanResponse(
        Long planId,
        Integer maxProjects,
        Integer maxTokensPerDay,
        Integer maxPreviews,
        Boolean unlimitedAi,
        Boolean active
) {
}
