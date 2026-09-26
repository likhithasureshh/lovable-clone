package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.entity.*;
import com.project.lovable_clone.enums.ChatEventType;
import com.project.lovable_clone.enums.MessageRole;
import com.project.lovable_clone.errors.ResourceNotFoundException;
import com.project.lovable_clone.llm.LlmResponseParser;
import com.project.lovable_clone.llm.PromptUtils;
import com.project.lovable_clone.llm.advisors.FileTreeContextAdvisor;
import com.project.lovable_clone.llm.tools.CodeGenerationTool;
import com.project.lovable_clone.repository.*;
import com.project.lovable_clone.security.AuthUtil;
import com.project.lovable_clone.service.AiGenerationService;
import com.project.lovable_clone.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiGenerationServiceImpl implements AiGenerationService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatClient chatClient;
    private final AuthUtil authUtil;
    private static final Pattern FILE_TAG_PATTERN = Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>", Pattern.DOTALL);
    private final ProjectFileService projectFileService;
    private final FileTreeContextAdvisor fileTreeAdvisor;
    private final LlmResponseParser llmResponseParser;
    private final ChatSessionRepository chatSessionRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ChatEventRepository chatEventRepository;

    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<String> streamResponse(String userMessage, Long projectId) {
         Long userId = authUtil.getCurrentUserId();
         ChatSession chatSession =canCreateChatSessionIfNotExists(userId,projectId);
        Map<String,Object> advisorParams = Map.of(
                "userId",userId,
                    "projectId",projectId
        );
        CodeGenerationTool codeGenerationTool = new CodeGenerationTool(projectFileService,projectId);
        StringBuilder fullResponseBuffer = new StringBuilder();

        AtomicReference<Long> startTime = new AtomicReference<>(System.currentTimeMillis());
        AtomicReference<Long> endTime = new AtomicReference<>(0L);

        return chatClient.prompt()
                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .tools(codeGenerationTool)
                .advisors(advisorSpec -> {
                    advisorSpec.params(advisorParams);
                    advisorSpec.advisors(fileTreeAdvisor);
                })
                .stream()
                .chatResponse()
                .doOnNext(response->
                {
                    if (response == null || response.getResult() == null) {
                        log.warn("Received ChatResponse without result: {}", response);
                        return;
                    }
                    log.info("ChatResponse = {}", response);
                    String content = response.getResult().getOutput().getText();
                    if(content!= null && !content.isEmpty() && endTime.get() == 0)
                    {
                        endTime.set(System.currentTimeMillis());
                    }
                    fullResponseBuffer.append(content);
                })
                .doOnComplete(()->{
                    Schedulers.boundedElastic().schedule(()->
                    {
                        //parseAndSaveFiles(fullResponseBuffer.toString(),projectId);
                        long duration = endTime.get()-startTime.get();
                        finalizeChats(userMessage,chatSession, fullResponseBuffer.toString(),duration);
                    });
                    log.info("Final output :{}",fullResponseBuffer.toString());
                })
                .doOnError(response-> log.error("Error Occurred while streaming the response for project{}",projectId,response))
                //.map(response -> response.getResult().getOutput().getText())
                .handle((resp, sink) -> {
                    var result = resp != null ? resp.getResult() : null;
                    var output = result != null ? result.getOutput() : null;
                    var text   = output != null ? output.getText() : null;

                    if (text != null && !text.isEmpty()) {
                        sink.next(text);
                    }
                    // else: ignore non-text events
                });

    }


    public void finalizeChats(String userMessage, ChatSession chatSession,String fullText,Long duration)
    {
          Long projectId = chatSession.getProject().getId();
           //store user message first
           chatMessageRepository.save(
                   ChatMessage.builder()
                           .chatSession(chatSession)
                           .role(MessageRole.USER)
                           .content(userMessage)
                           .build()
           );

           ChatMessage assistantChatMessage = chatMessageRepository.save(
                 ChatMessage.builder()
                         .chatSession(chatSession)
                         .role(MessageRole.ASSISTANT)
                         .content("Dummy message")
                         .build()
           );


           List<ChatEvent> chatEventList = llmResponseParser.parseChatEvents(fullText,assistantChatMessage);
           chatEventList.addFirst(ChatEvent
                .builder()
                           .chatMessage(assistantChatMessage)
                           .type(ChatEventType.THOUGHT)
                           .content("Thought for "+duration+"s")
                           .sequenceOrder(0)
                   .build());
           chatEventList.stream()
                   .filter(e -> e.getType() == ChatEventType.FILE_EDIT)
                   .forEach(e -> projectFileService.saveFiles(projectId,e.getFilePath(),e.getContent()));
           chatEventRepository.saveAll(chatEventList);

    }

    public void parseAndSaveFiles(String content, Long projectId)
    {
        Matcher matcher = FILE_TAG_PATTERN.matcher(content);
        while(matcher.find())
        {
            String filePath = matcher.group(1);
            String fileContent = matcher.group(2).trim();
            projectFileService.saveFiles(projectId,filePath,fileContent);
        }
    }

    public ChatSession canCreateChatSessionIfNotExists(Long userId, Long projectId) {
        ChatSession chatSession = chatSessionRepository.findById(
                new ChatSessionId(projectId,userId)).orElse(null);
        if(chatSession == null)
        {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(()-> new ResourceNotFoundException("project",projectId.toString()));

            User user = userRepository.findById(userId)
                    .orElseThrow(()-> new ResourceNotFoundException("user",userId.toString()));
            ChatSessionId chatSessionId = new ChatSessionId(projectId,userId);
            chatSession = ChatSession.builder()
                    .chatSessionId(chatSessionId)
                    .project(project)
                    .user(user)
                    .build();
            chatSession = chatSessionRepository.save(chatSession);
        }
        return chatSession;
    }
}
