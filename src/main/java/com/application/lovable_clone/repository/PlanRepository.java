package com.application.lovable_clone.repository;

import com.application.lovable_clone.entity.Plan;
import com.stripe.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long> {

    Optional<Plan> findByStripePriceId(String id);
}
