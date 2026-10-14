package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.subscription.PlanLimitsResponse;
import com.project.lovable_clone.dto.subscription.UsageTodayResponse;

public interface UsageService {
   void recordTokensUsage(Long userId,int actualTokens);
   void checkDailyTokenUsage();
}
