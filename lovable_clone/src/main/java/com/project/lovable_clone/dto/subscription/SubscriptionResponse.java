package com.project.lovable_clone.dto.subscription;

import com.project.lovable_clone.entity.Plan;

import java.time.Instant;

public record SubscriptionResponse(
        Plan planId,
        Integer tokensUsedThisCycle,
        String status,
        Instant periodEndTime

) {
}
