package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.dto.chat.ChatResponse;
import com.project.lovable_clone.entity.ChatMessage;
import com.project.lovable_clone.entity.ChatSession;
import com.project.lovable_clone.entity.ChatSessionId;
import com.project.lovable_clone.mapper.ChatMapper;
import com.project.lovable_clone.repository.ChatMessageRepository;
import com.project.lovable_clone.repository.ChatSessionRepository;
import com.project.lovable_clone.repository.UserRepository;
import com.project.lovable_clone.security.AuthUtil;
import com.project.lovable_clone.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
    private final AuthUtil authUtil;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatMapper;

    @Override
    public List<ChatResponse> getProjectChatHistory(Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        ChatSession chatSession = chatSessionRepository.getReferenceById
                (new ChatSessionId(projectId,userId));
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatSession(chatSession);
        return chatMapper.fromChatMessageList(chatMessages);
    }
}
