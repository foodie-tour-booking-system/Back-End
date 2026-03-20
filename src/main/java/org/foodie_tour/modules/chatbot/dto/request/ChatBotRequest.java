package org.foodie_tour.modules.chatbot.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatBotRequest {
    String conversationId;
    @NotBlank(message = "Nội dung câu hỏi không được để trống")
    String prompt;
}
