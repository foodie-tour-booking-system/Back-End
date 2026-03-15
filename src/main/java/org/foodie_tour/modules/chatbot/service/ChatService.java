package org.foodie_tour.modules.chatbot.service;

import org.foodie_tour.modules.chatbot.dto.request.ChatBotRequest;
import org.foodie_tour.modules.chatbot.dto.response.ChatBotResponse;

public interface ChatService {
    ChatBotResponse chat(ChatBotRequest request);
}
