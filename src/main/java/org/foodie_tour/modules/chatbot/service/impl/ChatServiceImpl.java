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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatServiceImpl implements ChatService {
    Map<String, ChatClient> clients;

    @NonFinal
    Map<String, ChatModelData> modelDataMap;

    @PostConstruct
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

    public ChatBotResponse chat(ChatBotRequest request) {
        String model = getAvailableModel();

        ChatClient chatClient = clients.get(model);

        String response = chatClient.prompt(request.getPrompt()).call().content();

        return new ChatBotResponse(response);
    }
}
