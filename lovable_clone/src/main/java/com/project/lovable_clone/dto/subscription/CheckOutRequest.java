package com.project.lovable_clone.dto.subscription;

import com.project.lovable_clone.entity.Plan;

public record CheckOutRequest(
        Plan planId
) {
}
