package com.ddaodan.MineChatGPT;

import java.util.LinkedList;
import java.util.Queue;

public class ConversationContext {
    private Queue<String> conversationHistory;
    private int maxHistorySize;

    public ConversationContext(int maxHistorySize) {
        this.maxHistorySize = maxHistorySize;
        this.conversationHistory = new LinkedList<>();
    }

    public void addMessage(String message) {
        if (conversationHistory.size() >= maxHistorySize) {
            conversationHistory.poll(); // Remove the oldest message
        }
        conversationHistory.offer(message); // Add the new message
    }

    public String getConversationHistory() {
        return String.join("\n", conversationHistory);
    }

    public void clearHistory() {
        conversationHistory.clear();
    }
}