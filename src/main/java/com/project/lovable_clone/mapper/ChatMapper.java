package com.project.lovable_clone.mapper;

import com.project.lovable_clone.dto.chat.ChatResponse;
import com.project.lovable_clone.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    List<ChatResponse> fromChatMessageList(List<ChatMessage> chatMessageList);
}
