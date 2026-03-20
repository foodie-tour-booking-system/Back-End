package org.foodie_tour.modules.chatbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatModel {
    CLIENT_KEY_1(30),
    CLIENT_KEY_2(30),
    CLIENT_KEY_3(30),
    CLIENT_KEY_4(30),
    ;
    final int limit;
}
