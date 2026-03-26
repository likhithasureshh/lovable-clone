package com.project.lovable_clone.entity;

import com.project.lovable_clone.enums.MessageRole;

import java.time.Instant;

public class ChatMessage {
    Long id;
    ChatSession chatSession;
    String content;
    Integer tokensUsed;
    String toolCalls;
    MessageRole role;
    Instant createdAt;
}
