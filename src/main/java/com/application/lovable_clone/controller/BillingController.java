package com.application.lovable_clone.controller;

import com.application.lovable_clone.dto.subscription.CheckOutResponse;
import com.application.lovable_clone.dto.subscription.CheckoutRequest;
import com.application.lovable_clone.dto.subscription.PlanResponse;
import com.application.lovable_clone.dto.subscription.PortalResponse;
import com.application.lovable_clone.service.PlanService;
import com.application.lovable_clone.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BillingController {
    private final PlanService planService;
    private final SubscriptionService subscriptionService;
    @GetMapping(path = "/api/plans")
    public ResponseEntity<PlanService> getAllPlans()
    {
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping(path = "/api/me/subscription")
    public ResponseEntity<SubscriptionService> getCurrentSubscription()
    {
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @PostMapping(path = "/api/stripe/checkout")
    public ResponseEntity<CheckOutResponse> createCheckOutUrl(
            @RequestBody CheckoutRequest request
    )
    {
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.createCheckOutUrl(request,userId));
    }

    @PostMapping(path = "/api/stripe/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal()
    {
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.openCustomerPortal(userId));
    }
}
