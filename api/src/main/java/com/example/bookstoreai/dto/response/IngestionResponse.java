package com.example.bookstoreai.dto.response;

public record IngestionResponse(
        boolean success,
        String message,
        int totalIndexed
) {}
