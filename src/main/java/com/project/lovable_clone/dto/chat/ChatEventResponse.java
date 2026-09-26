package com.project.lovable_clone.dto.chat;

import com.project.lovable_clone.entity.ChatMessage;
import com.project.lovable_clone.enums.ChatEventType;

public record ChatEventResponse(
        Long id,
        ChatMessage chatMessage,
        ChatEventType type,
        Integer sequenceOrder,
        String content,
        String filePath,
        String metadata

) {

}
