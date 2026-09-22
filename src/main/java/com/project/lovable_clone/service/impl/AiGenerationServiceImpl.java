package com.project.lovable_clone.service.impl;

import com.project.lovable_clone.entity.ProjectFile;
import com.project.lovable_clone.llm.PromptUtils;
import com.project.lovable_clone.security.AuthUtil;
import com.project.lovable_clone.service.AiGenerationService;
import com.project.lovable_clone.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiGenerationServiceImpl implements AiGenerationService {
    private final ChatClient chatClient;
    private final AuthUtil authUtil;
    private static final Pattern FILE_TAG_PATTERN = Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>", Pattern.DOTALL);
    private final ProjectFileService projectFileService;

    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<String> streamResponse(String userMessage, Long projectId) {
         Long userId = authUtil.getCurrentUserId();
         canCreateChatSessionIfNotExists(userId,projectId);
        Map<String,Object> advisorParams = Map.of(
                "userId",userId,
                    "projectId",projectId
        );
        StringBuilder fullResponseBuffer = new StringBuilder();

        return chatClient.prompt()
                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .advisors(advisorSpec -> {
                    advisorSpec.params(advisorParams);
                })
                .stream()
                .chatResponse()
                .doOnNext(response->
                {
                    String content = response.getResult().getOutput().getText();
                    fullResponseBuffer.append(content);
                })
                .doOnComplete(()->{
                    Schedulers.boundedElastic().schedule(()->
                    {
                        parseAndSaveFiles(fullResponseBuffer.toString(),projectId);
                    });
                })
                .doOnError(response-> log.error("Error Ocurred while streaming the response for project{}",projectId,response))
                .map(response -> Objects.requireNonNull(response.getResult()).getOutput().getText());

    }

    private void parseAndSaveFiles(String content, Long projectId)
    {
        Matcher matcher = FILE_TAG_PATTERN.matcher(content);
        while(matcher.find())
        {
            String filePath = matcher.group(1);
            String fileContent = matcher.group(2).trim();
            projectFileService.saveFiles(projectId,filePath,fileContent);
        }
    }

    private void canCreateChatSessionIfNotExists(Long userId, Long projectId) {
    }
}
