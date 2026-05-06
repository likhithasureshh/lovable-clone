package com.application.lovable_clone.dto.project;

import java.time.Instant;

public record ProjectSummaryResponse(
        Long id,
        String name,
        Instant updatedAt,
        Instant createdAt

) {
}
