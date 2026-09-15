package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.subscription.CheckOutRequest;
import com.project.lovable_clone.dto.subscription.CheckOutResponse;
import com.project.lovable_clone.dto.subscription.PortalResponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentProcessor {
    CheckOutResponse createCheckOutUrl(CheckOutRequest request);

    PortalResponse getCustomerPortalUrl();

    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);
}
