package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.subscription.PlanLimitsResponse;
import com.project.lovable_clone.dto.subscription.PlanResponse;
import com.project.lovable_clone.dto.subscription.SubscriptionResponse;
import com.project.lovable_clone.dto.subscription.UsageTodayResponse;
import com.project.lovable_clone.entity.UsageLog;
import com.project.lovable_clone.repository.UsageLogRepository;
import com.project.lovable_clone.security.AuthUtil;
import com.project.lovable_clone.service.SubscriptionService;
import com.project.lovable_clone.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
@RequiredArgsConstructor
@Service
public class UsageServiceImpl implements UsageService {

    private final UsageLogRepository usageLogRepository;
    private final AuthUtil authUtil;
    private final SubscriptionService subscriptionService;

    @Override
    public void recordTokensUsage(Long userId, int actualTokens) {
        LocalDate todayDate = LocalDate.now();
        UsageLog usageLog = usageLogRepository.findByUserIdAndDate(userId,todayDate)
                .orElseGet(()->createNewDailyUsage(userId,todayDate));
        usageLog.setTokensUsed(usageLog.getTokensUsed() + actualTokens);
        usageLogRepository.save(usageLog);
    }

    @Override
    public void checkDailyTokenUsage() {
        Long userId = authUtil.getCurrentUserId();
        SubscriptionResponse subscriptionResponse = subscriptionService.getMySubscription();
        PlanResponse planResponse = subscriptionResponse.plan();
        LocalDate todayDate = LocalDate.now();
        if(planResponse.unlimitedAi())
        {
            return;
        }
        UsageLog todayLog = usageLogRepository.findByUserIdAndDate(userId,todayDate)
                .orElseGet(()->createNewDailyUsage(userId,todayDate));
        int todayUsageToken = todayLog.getTokensUsed();
        int allowedTokenUsage = Math.toIntExact(planResponse.maxTokensPerDay());
        if(todayUsageToken >= allowedTokenUsage)
        {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,"Daily Limit Exceeded,Upgrade Now!");
        }
    }

    public UsageLog createNewDailyUsage(Long userId,LocalDate date)
    {
        return UsageLog
                .builder()
                .date(date)
                .userId(userId)
                .tokensUsed(0)
                .build();
    }
}
