package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.subscription.CheckOutRequest;
import com.project.lovable_clone.dto.subscription.CheckOutResponse;
import com.project.lovable_clone.dto.subscription.PortalResponse;
import com.project.lovable_clone.dto.subscription.SubscriptionResponse;
import com.project.lovable_clone.service.SubscriptionService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public SubscriptionResponse getCurrentSubscription(Long userId) {
        return null;
    }

    @Override
    public CheckOutResponse createCheckOutSessionurl(CheckOutRequest request, Long userId) {
        return null;
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }
}
