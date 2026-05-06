package com.application.lovable_clone.dto.subscription;

import java.time.Instant;

public record SubscriptionResponse(
        PlanResponse plan,
        String status,
        Instant periodEndDate,
        Long tokensUsedThisMonth
) {
}
