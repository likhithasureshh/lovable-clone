package com.application.lovable_clone.service;

import com.application.lovable_clone.dto.subscription.CheckOutResponse;
import com.application.lovable_clone.dto.subscription.CheckoutRequest;
import com.application.lovable_clone.dto.subscription.PortalResponse;
import com.application.lovable_clone.dto.subscription.SubscriptionResponse;
import com.application.lovable_clone.enums.SubscriptionStatus;

import java.time.Instant;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription();


    void activateSubscription(Long userId, Long planId, String stripeCustomerId, String subscriptionId);

    void updateSubscription(String id, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

    void cancelSubscription(String id);

    void renewSubscriptionPeriod(String subId, Instant currentPeriodStart, Instant currentPeriodEnd);

    void makeSubscriptionPastDue(String subId);
}
