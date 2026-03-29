package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.subscription.PlanLimitResponse;
import com.project.lovable_clone.dto.subscription.UsageTodayResponse;
import org.springframework.stereotype.Service;


public interface UsageService {
    UsageTodayResponse getTodayUsageOfUser(Long userId);

    PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId);
}
