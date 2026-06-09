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
import com.application.lovable_clone.repository.ProjectMemberRepository;
import com.application.lovable_clone.repository.SubscriptionRepository;
import com.application.lovable_clone.repository.UserRepository;
import com.application.lovable_clone.security.AuthUtil.AuthUtil;
import com.application.lovable_clone.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final AuthUtil authUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final ProjectMemberRepository projectMemberRepository;
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
    public void updateSubscription(String id, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId)
    {
       Subscription subscription = getSubscription(id);
       boolean hasSubscriptionChanged=false;
       if(status!=null && !subscription.getStatus().equals(status))
       {
           subscription.setStatus(status);
           hasSubscriptionChanged=true;
       }
        if(periodStart!=null && subscription.getCurrentPeriodStart() !=periodStart)
        {
            subscription.setCurrentPeriodStart(periodStart);
            hasSubscriptionChanged=true;
        }
        if(periodEnd!=null && subscription.getCurrentPeriodEnd() != periodEnd)
        {
            subscription.setCurrentPeriodEnd(periodEnd);
            hasSubscriptionChanged=true;
        }
        if(cancelAtPeriodEnd!=null && subscription.getCancelAtPeriodEnd() != cancelAtPeriodEnd)
        {
            subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
            hasSubscriptionChanged=true;
        }
        if(planId!=null && !subscription.getPlan().getId().equals(planId))
        {
            Plan plan = getPlan(planId);
            subscription.setPlan(plan);
            hasSubscriptionChanged=true;
        }
        if(hasSubscriptionChanged)
        {
            log.debug("subscription has been changed with id:{}",id);
            subscriptionRepository.save(subscription);
        }


    }

    @Override
    public void cancelSubscription(String id) 
    {
       Subscription subscription = getSubscription(id);
       subscription.setStatus(SubscriptionStatus.CANCELLED);
       subscriptionRepository.save(subscription);
    }

    @Override
    public void renewSubscriptionPeriod(String subId, Instant currentPeriodStart, Instant currentPeriodEnd) {
       Subscription subscription = getSubscription(subId);

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
        Subscription subscription = getSubscription(subId) ;
        if(subscription.getStatus().equals(SubscriptionStatus.PAST_DUE))
        {
            log.debug("the status of the subscription is already past due");
            return;
        }
        subscription.setStatus(SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);

    }
    private final Integer FREE_TIER_MAX_PROJECTS_ALLOWED = 1;
    @Override
    public boolean canCreateProjects() {
        Long userId = authUtil.getCurrentUserId();
        SubscriptionResponse subscription = getCurrentSubscription();
        int count = projectMemberRepository.maxProjectsAllowedToUserBySubscription(userId);
        if (subscription.plan() == null)
        {
            return count <FREE_TIER_MAX_PROJECTS_ALLOWED;
        }

        return count< subscription.plan().maxProjects();
    }


    //utility methods
    private  User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", userId.toString()));
    }

    private  Plan getPlan(Long planId) {
        return planRepository.findById(planId).orElseThrow(() -> new ResourceNotFoundException("plan", planId.toString()));
    }

    private Subscription getSubscription(String subId)
    {
        return subscriptionRepository.findByStripeSubscriptionId(subId).
                orElseThrow(()-> new ResourceNotFoundException("subscription", subId));
    }

}
