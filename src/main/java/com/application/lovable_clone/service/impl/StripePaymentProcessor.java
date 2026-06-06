package com.application.lovable_clone.service.impl;

import com.application.lovable_clone.dto.subscription.CheckOutResponse;
import com.application.lovable_clone.dto.subscription.CheckoutRequest;
import com.application.lovable_clone.dto.subscription.PortalResponse;
import com.application.lovable_clone.entity.Plan;
import com.application.lovable_clone.entity.User;
import com.application.lovable_clone.errors.ResourceNotFoundException;
import com.application.lovable_clone.repository.PlanRepository;
import com.application.lovable_clone.repository.UserRepository;
import com.application.lovable_clone.security.AuthUtil.AuthUtil;
import com.application.lovable_clone.service.PaymentProcessor;
import com.stripe.exception.StripeException;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
public class StripePaymentProcessor implements PaymentProcessor {
    private final PlanRepository planRepository;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    @Value("${client.url}")
    private String clientUrl;
    @Override
    public CheckOutResponse createCheckOutUrl(CheckoutRequest request) {

        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(()-> new ResourceNotFoundException("plan",request.planId().toString()));

        Long userId = authUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("user",userId.toString()));


        var params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(plan.getStripePriceId()).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSubscriptionData(
                        new SessionCreateParams.SubscriptionData.Builder()
                                .setBillingMode(SessionCreateParams.SubscriptionData.BillingMode.builder()
                                        .setType(SessionCreateParams.SubscriptionData.BillingMode.Type.FLEXIBLE)
                                        .build())
                                .build()
                )
                .setSuccessUrl(clientUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(clientUrl + "/success.html")
                .putMetadata("plan_id",plan.getId().toString())
                .putMetadata("user_id",userId.toString());

        try {

            String stripeCustomerId = user.getStripeCustomerId();
            if(stripeCustomerId == null || stripeCustomerId.isEmpty())
            {
                params.setCustomerEmail(user.getUsername());
            }
            else {
                params.setCustomer(stripeCustomerId);
            }
            Session session = Session.create(params.build());
            return new CheckOutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }

    @Override
    public void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata) {
       log.info(type);
    }
}
