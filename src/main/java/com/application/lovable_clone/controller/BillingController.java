package com.application.lovable_clone.controller;

import com.application.lovable_clone.dto.subscription.CheckOutResponse;
import com.application.lovable_clone.dto.subscription.CheckoutRequest;
import com.application.lovable_clone.dto.subscription.PlanResponse;
import com.application.lovable_clone.dto.subscription.PortalResponse;
import com.application.lovable_clone.service.PaymentProcessor;
import com.application.lovable_clone.service.PlanService;
import com.application.lovable_clone.service.SubscriptionService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BillingController {
    private final PlanService planService;
    private final SubscriptionService subscriptionService;
    private final PaymentProcessor paymentProcessor;


    @Value("${stripe.webhook.secret-key}")
    private String webhookSecret;
    @GetMapping(path = "/api/plans")
    public ResponseEntity<PlanService> getAllPlans()
    {
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping(path = "/api/me/subscription")
    public ResponseEntity<SubscriptionService> getCurrentSubscription()
    {
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @PostMapping(path = "/api/payment/checkout")
    public ResponseEntity<CheckOutResponse> createCheckOutUrl(
            @RequestBody CheckoutRequest request
    )
    {
        return ResponseEntity.ok(paymentProcessor.createCheckOutUrl(request));
    }

    @PostMapping(path = "/api/payment/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal()
    {
        Long userId = 1L;
        return ResponseEntity.ok(paymentProcessor.openCustomerPortal(userId));
    }

    @PostMapping(path = "webhook/payments")
    public ResponseEntity<String> webhookEvents(
            @RequestBody String payload,
            @RequestHeader(name = "Stripe-Signature") String signHeader
    )
    {
        try {
            Event event = Webhook.constructEvent(payload, signHeader, webhookSecret);

            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;

            log.info("Event API version: {}", event.getApiVersion());
            log.info("SDK API version: {}", Stripe.API_VERSION);

            if (deserializer.getObject().isPresent()) { // happy case
                stripeObject = deserializer.getObject().get();
            } else {
                // Fallback: Deserialize from raw JSON
                try {
                    stripeObject = deserializer.deserializeUnsafe();
                    if (stripeObject == null) {
                        log.warn("Failed to deserialize webhook object for event: {}", event.getType());
                        return ResponseEntity.ok().build();
                    }
                } catch (Exception e) {
                    log.error("Unsafe deserialization failed for event {}: {}", event.getType(), e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Deserialization failed");
                }
            }

            // Now extract metadata only if it's a Checkout Session
            Map<String, String> metadata = new HashMap<>();
            if (stripeObject instanceof Session session) {
                metadata = session.getMetadata();
            }

            // Pass to your processor
            paymentProcessor.handleWebhookEvent(event.getType(), stripeObject, metadata);
            return ResponseEntity.ok().build();

        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }
    }
}
