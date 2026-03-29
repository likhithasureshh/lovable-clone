package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.subscription.CheckOutRequest;
import com.project.lovable_clone.dto.subscription.CheckOutResponse;
import com.project.lovable_clone.dto.subscription.PortalResponse;
import com.project.lovable_clone.dto.subscription.SubscriptionResponse;
import org.springframework.stereotype.Service;


public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription(Long userId);

    CheckOutResponse createCheckOutSessionurl(CheckOutRequest request, Long userId);

    PortalResponse openCustomerPortal(Long userId);
}
