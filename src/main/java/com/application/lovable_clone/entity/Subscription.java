package com.application.lovable_clone.entity;

import com.application.lovable_clone.enums.SubscriptionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription {

    Long id;
    User user;
    Plan plan;
    String stripeSubscriptionId;
    String stripeCustomerId;
    Instant currentPeriodStart;
    Instant currentPeriodEnd;
    Boolean cancelAtPeriodEnd;
    @Enumerated(EnumType.STRING)
    SubscriptionStatus status;
    Instant createdAt;
    Instant updatedAt;
}
