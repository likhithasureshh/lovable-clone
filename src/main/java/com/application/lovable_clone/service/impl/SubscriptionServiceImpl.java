package com.application.lovable_clone.service.impl;

import com.application.lovable_clone.dto.subscription.CheckOutResponse;
import com.application.lovable_clone.dto.subscription.CheckoutRequest;
import com.application.lovable_clone.dto.subscription.PortalResponse;
import com.application.lovable_clone.dto.subscription.SubscriptionResponse;
import com.application.lovable_clone.entity.Plan;
import com.application.lovable_clone.entity.Subscription;
import com.application.lovable_clone.entity.User;
import com.application.lovable_clone.enums.SubscriptionStatus;
import com.application.lovable_clone.errors.ResourceNotFoundException;
import com.application.lovable_clone.mapper.SubscriptionMapper;
import com.application.lovable_clone.repository.PlanRepository;
import com.application.lovable_clone.repository.SubscriptionRepository;
import com.application.lovable_clone.repository.UserRepository;
import com.application.lovable_clone.security.AuthUtil.AuthUtil;
import com.application.lovable_clone.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final AuthUtil authUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    @Override
    public SubscriptionResponse getCurrentSubscription() 
    {
        Long userId = authUtil.getCurrentUserId();
        Subscription subscription =  subscriptionRepository.findByUserIdAndStatusIn(userId, Set.of(
                SubscriptionStatus.ACTIVE,SubscriptionStatus.PAST_DUE,SubscriptionStatus.TRAILING
        )).orElse(new Subscription());
        return subscriptionMapper.toSubscriptionResponse(subscription);
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String stripeCustomerId, String subscriptionId) 
    {
         Boolean exists = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
         if(exists) return;
         User user = getUser(userId);
        Plan plan = getPlan(planId);
        Subscription subscription = Subscription
                 .builder()
                .user(user)
                .plan(plan)
                .stripeSubscriptionId(subscriptionId)
                .status(SubscriptionStatus.INCOMPLETE)
                 .build();
        subscriptionRepository.save(subscription);
    }

    @Override
    public void updateSubscription(String id, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {

    }

    @Override
    public void cancelSubscription(String id) {

    }

    @Override
    public void renewSubscriptionPeriod(String subId, Instant currentPeriodStart, Instant currentPeriodEnd) {
       Subscription subscription = subscriptionRepository.findByStripeSubscriptionId(subId).
               orElseThrow(()-> new ResourceNotFoundException("subscription", subId));

       Instant newStart = currentPeriodStart!=null?currentPeriodStart:subscription.getCurrentPeriodEnd();
       subscription.setCurrentPeriodStart(newStart);
       subscription.setCurrentPeriodEnd(currentPeriodEnd);
       if(subscription.getStatus().equals(SubscriptionStatus.PAST_DUE)||subscription.getStatus().equals(SubscriptionStatus.INCOMPLETE))
       {
           subscription.setStatus(SubscriptionStatus.ACTIVE);
       }
       subscriptionRepository.save(subscription);
    }

    @Override
    public void makeSubscriptionPastDue(String subId) {

    }
    
    
    //utility methods
    private  User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", userId.toString()));
    }

    private  Plan getPlan(Long planId) {
        return planRepository.findById(planId).orElseThrow(() -> new ResourceNotFoundException("plan", planId.toString()));
    }


}
