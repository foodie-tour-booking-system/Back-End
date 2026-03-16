package org.foodie_tour.modules.chatbot.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.foodie_tour.modules.chatbot.dto.request.ChatBotRequest;
import org.foodie_tour.modules.chatbot.dto.response.ChatBotResponse;
import org.foodie_tour.modules.chatbot.enums.ChatModel;
import org.foodie_tour.modules.chatbot.service.ChatService;
import org.foodie_tour.modules.chatbot.utils.ChatModelData;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatServiceImpl implements ChatService {
    Map<String, ChatClient> clients;

    @NonFinal
    Map<String, ChatModelData> modelDataMap;

    @NonFinal
    Map<LocalDateTime, Set<String>> timeConversationMap;

    ChatMemoryRepository chatMemoryRepository;

    @PostConstruct
    @Scheduled(cron = "0 0 0 * * *")
    public void resetModelMapData() {
        modelDataMap = new ConcurrentHashMap<>();
        clients.forEach((k,v) -> {
            int limit = ChatModel.valueOf(k).getLimit();
            modelDataMap.put(k, new ChatModelData(limit));
        });
    }

    private String getAvailableModel() {
        LocalDateTime now = LocalDateTime.now();

        // Default value
        String model = null;
        int max = 0;

        // Loop to find max
        for (Map.Entry<String, ChatModelData> entry : modelDataMap.entrySet()) {
            int remain = entry.getValue().getRemain(now);

            if (remain > max) {
                model = entry.getKey();
                max = remain;
            }
        }

        // If all model out of request -> throw exception
        if (max == 0 || model == null) {
            throw new RuntimeException("Vui lòng thử lại sau");
        }

        ChatModelData modelUsage = modelDataMap.get(model);
        if (!modelUsage.tryUse(now)) {
            throw new RuntimeException("Vui lòng thử lại sau");
        }

        return model;
    }

    public String createNewConversation() {
        return UUID.randomUUID().toString();
    }

    @PostConstruct
    public void initTimeConversationMap() {
        timeConversationMap = new ConcurrentHashMap<>();
    }

    public ChatBotResponse chat(ChatBotRequest request) {
        String model = getAvailableModel();
        String conversationId = request.getConversationId();

        ChatClient chatClient = clients.get(model);

        // Put conversation id to map
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        timeConversationMap.computeIfAbsent(now, k -> new CopyOnWriteArraySet<>()).add(conversationId);

        String response = chatClient.prompt(request.getPrompt())
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call().content();

        return new ChatBotResponse(response);
    }

    @Scheduled(cron = "0 * * * * *")
    public void cleanUpConversation() {
        // Get expired time point
        LocalDateTime expiredTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minusMinutes(30);

        // Find all expired time
        List<LocalDateTime> expiredTimeList = timeConversationMap.keySet().stream().filter(k -> k.isBefore(expiredTime)).toList();

        // Find all conversation expired
        List<String> expiredConversation = expiredTimeList.stream().map(k -> timeConversationMap.get(k)).flatMap(Collection::stream).toList();

        // Delete expired time in map
        expiredTimeList.forEach(time -> timeConversationMap.remove(time));

        // Delete expired conversation
        expiredConversation.forEach(chatMemoryRepository::deleteByConversationId);
    }
}
