package org.foodie_tour.modules.chatbot.service;

import org.foodie_tour.modules.chatbot.dto.request.ChatBotRequest;
import org.foodie_tour.modules.chatbot.dto.response.ChatBotResponse;

public interface ChatService {
    String createNewConversation();
    ChatBotResponse chat(ChatBotRequest request);
}
