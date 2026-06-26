package com.example.bookstoreai.controller;

import com.example.bookstoreai.dto.ai.ChatRequest;
import com.example.bookstoreai.dto.ai.ChatResponse;
import com.example.bookstoreai.service.IAgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final IAgentService agentService;

    @PostMapping("/chat")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER')")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(agentService.chat(request.message(), request.conversationId()));
    }

    @DeleteMapping("/conversations/{conversationId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER')")
    public ResponseEntity<Void> deleteConversation(@PathVariable String conversationId) {
        agentService.deleteConversation(conversationId);
        return ResponseEntity.noContent().build();
    }
}
