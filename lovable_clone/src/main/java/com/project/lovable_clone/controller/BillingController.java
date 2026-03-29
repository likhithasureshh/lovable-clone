package com.project.lovable_clone.controller;


import com.project.lovable_clone.dto.subscription.*;
import com.project.lovable_clone.service.PlanService;
import com.project.lovable_clone.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BillingController {
    private final SubscriptionService subscriptionService;
    private final PlanService planService;


    @GetMapping("/plans")
    public ResponseEntity<PlanResponse> getAllPlans()
    {
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping(path = "/me/subscription")
    public ResponseEntity<SubscriptionResponse> getMySubscription()
    {
        Long userId =1L;
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @PostMapping(path = "/stripe/checkout")
    public ResponseEntity<CheckOutResponse> createCheckOutResponse(
            @RequestBody CheckOutRequest request
    )
    {
        Long userId=1L;
        return ResponseEntity.ok(subscriptionService.createCheckOutSessionurl(request,userId));
    }

    @PostMapping(path = "/stripe/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal()
    {
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.openCustomerPortal(userId));
    }

}
