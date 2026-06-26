package com.example.bookstoreai.service;

import com.example.bookstoreai.dto.ai.ChatResponse;

public interface IAgentService {
    ChatResponse chat(String message, String conversationId);
    void deleteConversation(String conversationId);
}
