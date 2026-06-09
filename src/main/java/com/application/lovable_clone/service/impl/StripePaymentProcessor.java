package com.application.lovable_clone.service.impl;

import com.application.lovable_clone.dto.subscription.CheckOutResponse;
import com.application.lovable_clone.dto.subscription.CheckoutRequest;
import com.application.lovable_clone.dto.subscription.PortalResponse;
import com.application.lovable_clone.entity.Plan;
import com.application.lovable_clone.entity.User;
import com.application.lovable_clone.enums.SubscriptionStatus;
import com.application.lovable_clone.errors.ResourceNotFoundException;
import com.application.lovable_clone.repository.PlanRepository;
import com.application.lovable_clone.repository.UserRepository;
import com.application.lovable_clone.security.AuthUtil.AuthUtil;
import com.application.lovable_clone.service.PaymentProcessor;
import com.application.lovable_clone.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
public class StripePaymentProcessor implements PaymentProcessor {
    private final PlanRepository planRepository;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    @Value("${client.url}")
    private String clientUrl;
    @Override
    public CheckOutResponse createCheckOutUrl(CheckoutRequest request) {

        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(()-> new ResourceNotFoundException("plan",request.planId().toString()));

        Long userId = authUtil.getCurrentUserId();
        User user = getUser(userId);


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
    public PortalResponse openCustomerPortal()
    {
       Long userId = authUtil.getCurrentUserId();
       User user = getUser(userId);
       String stripeCustomerId = user.getStripeCustomerId();
        try {
            var sessions = com.stripe.model.billingportal.Session.create(
                    com.stripe.param.billingportal.SessionCreateParams
                            .builder()
                            .setCustomer(stripeCustomerId)
                            .setReturnUrl(clientUrl)
                            .build()
            );
            return new PortalResponse(sessions.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata) {
       log.debug("Handling the event type: {}",type);
       switch (type)
       {
           case "checkout.session.completed"-> handleCheckoutSessionCompleted((Session) stripeObject,metadata);
           case "customer.subscription.updated"-> handleCustomerSubscriptionUpdated((Subscription) stripeObject);
           case "customer.subscription.deleted"-> handleCustomerSubscriptionDeleted((Subscription) stripeObject);
           case "invoice.paid"-> handleInvoicePaid((Invoice)stripeObject);
           case "invoice.payment_failed"-> handleInvoicePaymentFailed((Invoice)stripeObject);
           default->{
               log.debug("Invalid event ignored:{}",type);
           }
       }
    }

    private void handleInvoicePaymentFailed(Invoice invoice)
    {
        String subId = extractSubscriptionFromInvoice(invoice);
        if(subId==null) return;
        subscriptionService.makeSubscriptionPastDue(subId);
    }

    private void handleInvoicePaid(Invoice invoice)  {
        String subId = extractSubscriptionFromInvoice(invoice);
        if(subId==null) return;
        try {
            Subscription subscription = Subscription.retrieve(subId);
            var item = subscription.getItems().getData().get(0);
            Instant currentPeriodStart = Instant.ofEpochSecond(item.getCurrentPeriodStart());
            Instant currentPeriodEnd = Instant.ofEpochSecond(item.getCurrentPeriodEnd());

            subscriptionService.renewSubscriptionPeriod(subId,currentPeriodStart,currentPeriodEnd);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }

    private String extractSubscriptionFromInvoice(Invoice invoice)
    {
       var parent = invoice.getParent();
       if(parent==null) return null;
       var subDetails = parent.getSubscriptionDetails();
       if (subDetails==null) return null;
       return subDetails.getSubscription();

    }

    private void handleCustomerSubscriptionDeleted(Subscription subscription)
    {
        if(subscription==null)
        {
            log.debug("subscription onject is null inside handleCustomerSubscriptionDeleted");
        }
        subscriptionService.cancelSubscription(subscription.getId());
    }

    private void handleCustomerSubscriptionUpdated(Subscription subscription)
    {

         if(subscription == null)
         {
             log.error("Error occurred in handleCustomerSubscriptionUpdated");
             return;
         }

        SubscriptionStatus status = mapSubscriptionStatusToEnum(subscription.getStatus());
         if(status==null)
         {
             log.debug("status is null");
             return;
         }
         SubscriptionItem item = subscription.getItems().getData().get(0);
         Instant periodStart = Instant.ofEpochSecond(item.getCurrentPeriodStart());
         Instant periodEnd = Instant.ofEpochSecond(item.getCurrentPeriodEnd());
         Boolean cancelAtPeriodEnd =subscription.getCancelAtPeriodEnd();

         Long planId = resolvePlanId(item.getPrice()).getId();
         subscriptionService.updateSubscription(subscription.getId(),status,periodStart,periodEnd,cancelAtPeriodEnd,planId);

    }

    private SubscriptionStatus mapSubscriptionStatusToEnum(String status)
    {
        return switch(status)
        {
            case "active"->SubscriptionStatus.ACTIVE;
            case "trailing"->SubscriptionStatus.TRAILING;
            case "past_due","unpaid","paused","incomplete_expired"->SubscriptionStatus.PAST_DUE;
            case "canceled"->SubscriptionStatus.CANCELLED;
            case "incomplete"->SubscriptionStatus.INCOMPLETE;
            default -> {
                log.debug("Invalid subscription status");
                yield null;
            }
        };
    }

    private void handleCheckoutSessionCompleted(Session stripeObject, Map<String, String> metadata)
    {
        if(stripeObject == null)
        {
            log.debug("Error occurred in handleCheckoutSessionCompleted");
            return;
        }
        Long userId = Long.valueOf(metadata.get("user_id"));
        Long planId = Long.valueOf(metadata.get("plan_id"));

        String stripeCustomerId = stripeObject.getCustomer();
        String subscriptionId = stripeObject.getSubscription();

        User user = getUser(userId);
        if(user.getStripeCustomerId()==null)
        {
            user.setStripeCustomerId(stripeCustomerId);
            userRepository.save(user);
        }
        subscriptionService.activateSubscription(userId,planId,stripeCustomerId,subscriptionId);

    }

    private User getUser(Long userId)
    {
        return userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("user",userId.toString()));
    }
    private Plan resolvePlanId(Price stripePriceId)
    {
       return  planRepository.findByStripePriceId(stripePriceId.getId()).orElse(null);
    }


}
