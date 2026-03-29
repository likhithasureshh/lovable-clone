package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.subscription.PlanResponse;
import org.springframework.stereotype.Service;


public interface PlanService {
    PlanResponse getAllActivePlans();
}
