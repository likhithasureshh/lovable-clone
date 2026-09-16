package com.project.lovable_clone.repository;

import com.project.lovable_clone.entity.Subscription;
import com.project.lovable_clone.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    Optional<Subscription> findByUserIdAndStatusIn(Long userId, Set<SubscriptionStatus> active);

    Optional<Subscription> findByStripeSubscriptionId(String subscriptionId);

    boolean existsByStripeSubscriptionId(String subscriptionId);
}
