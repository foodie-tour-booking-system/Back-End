package org.foodie_tour.modules.chatbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatModel {
    LLAMA3_8B(30),
    LLAMA3_70B(30),
    GPT_OSS_120B(30),
    GPT_OSS_20B(30),
    LLAMA4_17B(30),
    ;
    final int limit;
}
