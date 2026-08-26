package com.project.lovable_clone.controller;

import com.project.lovable_clone.dto.subscription.PlanLimitsResponse;
import com.project.lovable_clone.dto.subscription.UsageTodayResponse;
import com.project.lovable_clone.service.UsageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/api/usage")
public class UsageController {
    private final UsageService usageService;
    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> getUsageToday()
    {
        Long userId = 1L;
        return ResponseEntity.ok(usageService.getUsageToday(userId));
    }

    @GetMapping("/limits")
    public ResponseEntity<PlanLimitsResponse> getCurrentSubscriptionLimitsForTheUser()
    {
        Long userId = 1L;
        return ResponseEntity.ok(usageService.getCurrentSubscriptionLimitsForTheUser(userId));
    }
}
