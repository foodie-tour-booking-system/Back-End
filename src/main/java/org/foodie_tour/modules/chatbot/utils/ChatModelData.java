package org.foodie_tour.modules.chatbot.utils;

import lombok.Getter;

import java.time.LocalDateTime;

public class ChatModelData {
    @Getter
    private LocalDateTime firstTimeCall;
    private int remain;
    private int limit;

    public ChatModelData(int limit) {
        this.limit = limit;
        this.firstTimeCall = null;
        this.remain = limit;
    }

    public synchronized int getRemain(LocalDateTime now) {
        if (firstTimeCall == null || now.isAfter(firstTimeCall.plusMinutes(1))) {
            firstTimeCall = now;
            remain = limit;
        }
        return remain;
    }

    public synchronized boolean tryUse(LocalDateTime now) {
        if (firstTimeCall == null || now.isAfter(firstTimeCall.plusMinutes(1))) {
            firstTimeCall = now;
            remain = limit;
        }

        if (remain > 0) {
            remain--;
            return true;
        }

        return false;
    }
}