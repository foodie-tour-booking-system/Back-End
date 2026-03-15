package org.foodie_tour.modules.chatbot.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.chatbot.dto.request.ChatBotRequest;
import org.foodie_tour.modules.chatbot.dto.response.ChatBotResponse;
import org.foodie_tour.modules.chatbot.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/rag-chat")
public class ChatController {
    ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatBotResponse> chat(@RequestBody ChatBotRequest request){
        var result = chatService.chat(request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
