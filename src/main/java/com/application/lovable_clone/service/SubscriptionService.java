package com.application.lovable_clone.service;

import com.application.lovable_clone.dto.subscription.CheckOutResponse;
import com.application.lovable_clone.dto.subscription.CheckoutRequest;
import com.application.lovable_clone.dto.subscription.PortalResponse;

public interface SubscriptionService {
    SubscriptionService getCurrentSubscription(Long userId);

    CheckOutResponse createCheckOutUrl(CheckoutRequest request, Long userId);

    PortalResponse openCustomerPortal(Long userId);
}
