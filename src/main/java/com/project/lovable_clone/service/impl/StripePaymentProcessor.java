package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.subscription.CheckOutRequest;
import com.project.lovable_clone.dto.subscription.CheckOutResponse;
import com.project.lovable_clone.dto.subscription.PlanResponse;
import com.project.lovable_clone.dto.subscription.PortalResponse;
import com.project.lovable_clone.entity.Plan;
import com.project.lovable_clone.entity.User;
import com.project.lovable_clone.errors.ResourceNotFoundException;
import com.project.lovable_clone.repository.PlanRepository;
import com.project.lovable_clone.repository.UserRepository;
import com.project.lovable_clone.security.AuthUtil;
import com.project.lovable_clone.service.PaymentProcessor;
import com.stripe.exception.StripeException;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.model.tax.Registration;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripePaymentProcessor implements PaymentProcessor {
    @Value("${client.url}")
    private String frontEndUrl;
    private final AuthUtil authUtil;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    @Override
    public CheckOutResponse createCheckOutUrl(CheckOutRequest request) {
        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(()-> new ResourceNotFoundException("plan",request.planId().toString()));
        Long userId = authUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("user",userId.toString()));
        var params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(plan.getStripePriceId()).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSubscriptionData(new SessionCreateParams.SubscriptionData.Builder()
                        .setBillingMode(SessionCreateParams.SubscriptionData.BillingMode.builder()
                                .setType(SessionCreateParams.SubscriptionData.BillingMode.Type.FLEXIBLE)
                                .build())
                        .build())
                .setSuccessUrl(frontEndUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontEndUrl+"/cancel.html")
                .putMetadata("planId",plan.getId().toString())
                .putMetadata("userId",userId.toString());
        try {
            String stripeCustomerId = user.getStripeCustomerId();
            if(stripeCustomerId==null || stripeCustomerId.isEmpty())
            {
                params.setCustomerEmail(user.getUsername());
            }
            else
            {
                params.setCustomer(stripeCustomerId);
            }
            Session session = Session.create(params.build());
            return new CheckOutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PortalResponse getCustomerPortalUrl() {
        return null;
    }

    @Override
    public void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata) {
        log.info(type);
    }
}
