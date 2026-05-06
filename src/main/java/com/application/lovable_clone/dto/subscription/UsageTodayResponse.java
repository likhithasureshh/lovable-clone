package com.application.lovable_clone.dto.subscription;

public record UsageTodayResponse(
        Integer previewsRunning,
        Integer previewLimit,
        Integer tokensUsed,
        Integer tokensLimit
) {
}
