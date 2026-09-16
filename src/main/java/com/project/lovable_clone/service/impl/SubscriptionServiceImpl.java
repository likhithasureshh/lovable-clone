package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.subscription.SubscriptionResponse;
import com.project.lovable_clone.entity.Plan;
import com.project.lovable_clone.entity.Subscription;
import com.project.lovable_clone.entity.User;
import com.project.lovable_clone.enums.SubscriptionStatus;
import com.project.lovable_clone.errors.ResourceNotFoundException;
import com.project.lovable_clone.mapper.SubscriptionMapper;
import com.project.lovable_clone.repository.PlanRepository;
import com.project.lovable_clone.repository.ProjectMemberRepository;
import com.project.lovable_clone.repository.SubscriptionRepository;
import com.project.lovable_clone.repository.UserRepository;
import com.project.lovable_clone.security.AuthUtil;
import com.project.lovable_clone.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final AuthUtil authUtil;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final Integer FREE_TIER_PROJECTS_ALLOWED = 1;

    @Override
    public SubscriptionResponse getMySubscription()
    {
        Long userId = authUtil.getCurrentUserId();
        Subscription fetchedSubscription = subscriptionRepository.findByUserIdAndStatusIn(userId,
                Set.of(
                        SubscriptionStatus.ACTIVE,SubscriptionStatus.PAST_DUE,SubscriptionStatus.TRIALING
                )).orElse(new Subscription());
        return subscriptionMapper.toSubscriptionResponse(fetchedSubscription);
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId)
    {
        boolean exists = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
        if(exists)
        {
            return;
        }
        Subscription subscription = Subscription
                .builder()
                .user(getUser(userId))
                .plan(getPlan(planId))
                .status(SubscriptionStatus.INCOMPLETE)
                .stripeSubscriptionId(subscriptionId)
                .build();
        subscriptionRepository.save(subscription);
    }

    @Override
    public void updateSubscription(String subId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId)
    {
         Subscription subscription = getSubscription(subId);
         boolean isSubscriptionUpdated = false;
         if(status != null && subscription.getStatus() != status)
         {
             isSubscriptionUpdated=true;
             subscription.setStatus(status);
         }
         if(periodStart!= null && !periodStart.equals(subscription.getCurrentPeriodStart()))
         {
             isSubscriptionUpdated=true;
             subscription.setCurrentPeriodStart(periodStart);
         }
         if(periodEnd != null && periodEnd.equals(subscription.getCurrentPeriodEnd()))
         {
             isSubscriptionUpdated=true;
             subscription.setCurrentPeriodEnd(periodEnd);
         }
         if(cancelAtPeriodEnd != null && !cancelAtPeriodEnd.equals(subscription.getCancelAtPeriodEnd()))
         {
             isSubscriptionUpdated=true;
             subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
         }
         if(planId != null && !planId.equals(subscription.getPlan().getId()))
         {
             isSubscriptionUpdated=true;
             Plan newPlan = getPlan(planId);
             subscription.setPlan(newPlan);
         }

        if(isSubscriptionUpdated)
        {
            log.debug("The Subscription details has been updated for the subsId:{}",subId);
            subscriptionRepository.save(subscription);
        }
    }

    @Override
    public void cancelSubscription(String subscriptionId)
    {
         Subscription subscription = getSubscription(subscriptionId);
         subscription.setStatus(SubscriptionStatus.CANCELED);
         subscriptionRepository.save(subscription);
    }

    @Override
    public void renewSubscriptionPeriod(String subId, Instant periodStart, Instant periodEnd) 
    {
        Subscription subscription = getSubscription(subId);
         periodStart = periodStart !=null ? periodStart : subscription.getCurrentPeriodEnd();
         subscription.setCurrentPeriodStart(periodStart);
         subscription.setCurrentPeriodEnd(periodEnd);
         if(subscription.getStatus() == SubscriptionStatus.PAST_DUE || subscription.getStatus() == SubscriptionStatus.INCOMPLETE)
         {
             subscription.setStatus(SubscriptionStatus.ACTIVE);
         }
         subscriptionRepository.save(subscription);

    }

    @Override
    public void makeSubscriptionPastDew(String subId)
    {
        Subscription subscription = getSubscription(subId);
        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE)
        {
            log.debug("The Subscription Status is already PAST_DEW");
            return;
        }
        subscription.setStatus(SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);
    }

    @Override
    public boolean canCreateNewProjects() {
        Long userId = authUtil.getCurrentUserId();
        int countOfOwnedProjects = projectMemberRepository.findUserOwnedProjects(userId);
        SubscriptionResponse subscriptionResponse = getMySubscription();
        if(subscriptionResponse.plan() == null)
        {
            return countOfOwnedProjects<FREE_TIER_PROJECTS_ALLOWED;
        }
        return countOfOwnedProjects < subscriptionResponse.plan().maxProjects();
    }

    public User getUser(Long userId)
    {
        return userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("user",userId.toString()));
    }
    
    public Plan getPlan(Long planId)
    {
        return planRepository.findById(planId)
                .orElseThrow(()-> new ResourceNotFoundException("plan",planId.toString()));
    }

    public Subscription getSubscription(String subId)
    {
        return subscriptionRepository.findByStripeSubscriptionId(subId)
                .orElseThrow(()->new ResourceNotFoundException("subscription", subId));
    }

}
