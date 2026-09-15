package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.subscription.CheckOutRequest;
import com.project.lovable_clone.dto.subscription.CheckOutResponse;
import com.project.lovable_clone.dto.subscription.PortalResponse;
import com.project.lovable_clone.dto.subscription.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse getMySubscription(Long userId);


}
