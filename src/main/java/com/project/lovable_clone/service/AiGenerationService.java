package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.chat.StreamResponse;
import reactor.core.publisher.Flux;

public interface AiGenerationService {
    Flux<StreamResponse> streamResponseFromLLM(String message, Long projectId);
}
