package com.project.lovable_clone.entity;

import java.time.Instant;

public class Preview {
    Long id;
    Project project;
    String namespace;
    String podName;
    String previewUrl;
    Instant startedAt;
    Instant terminatedAt;
    Instant createdAt;
}
