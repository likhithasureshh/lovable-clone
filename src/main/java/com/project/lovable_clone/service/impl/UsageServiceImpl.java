package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.subscription.PlanLimitsResponse;
import com.project.lovable_clone.dto.subscription.UsageTodayResponse;
import com.project.lovable_clone.service.UsageService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {
    @Override
    public UsageTodayResponse getUsageToday(Long userId) {
        return null;
    }

    @Override
    public PlanLimitsResponse getCurrentSubscriptionLimitsForTheUser(Long userId) {
        return null;
    }
}
