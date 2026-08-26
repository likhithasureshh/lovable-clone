package com.project.lovable_clone.controller;

import com.project.lovable_clone.dto.subscription.*;
import com.project.lovable_clone.service.PlanService;
import com.project.lovable_clone.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BillingController {
    private final PlanService planService;
    private final SubscriptionService subscriptionService;

    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanResponse>> getAllPlans()
    {
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getMySubscription()
    {
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.getMySubscription(userId));
    }

    @PostMapping("/api/stripe/checkout")
    public ResponseEntity<CheckOutResponse> checkOut(@RequestBody CheckOutRequest request)
    {
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.createCheckOutUrl(request,userId));
    }

    @PostMapping("/api/stripe/portal")
    public ResponseEntity<PortalResponse> getCustomerPortalUrl()
    {
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.getCustomerPortalUrl(userId));
    }

}
