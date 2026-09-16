package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.subscription.CheckOutRequest;
import com.project.lovable_clone.dto.subscription.CheckOutResponse;
import com.project.lovable_clone.dto.subscription.PortalResponse;
import com.project.lovable_clone.dto.subscription.SubscriptionResponse;
import com.project.lovable_clone.enums.SubscriptionStatus;

import java.time.Instant;

public interface SubscriptionService {
    SubscriptionResponse getMySubscription();


    void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId);

    void updateSubscription(String subscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

    void cancelSubscription(String subscriptionId);

    void renewSubscriptionPeriod(String subId, Instant periodStart, Instant periodEnd);

    void makeSubscriptionPastDew(String subId);

    boolean canCreateNewProjects();
}
