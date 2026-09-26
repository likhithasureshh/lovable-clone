package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.chat.ChatResponse;
import com.project.lovable_clone.entity.ChatSession;


import java.util.List;

public interface ChatService {

    List<ChatResponse> getProjectChatHistory(Long projectId);
}
