package com.project.lovable_clone.dto.chat;

import com.project.lovable_clone.entity.ChatEvent;
import com.project.lovable_clone.enums.MessageRole;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

public record ChatResponse(

        Long id,
        List<ChatEvent> events,
        String content,
        MessageRole role,
        Integer tokensUsed,
        Instant createdAt
) {
}
