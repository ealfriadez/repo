package com.example.bookstoreai.dto.ai;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
    @NotBlank(message = "El mensaje no puede estar vacio")
    String message,
    String conversationId
) {}
