package com.example.bookstoreai.dto.response;

public record ReviewResponse(
        Long id,
        String userName,
        String comment,
        Integer rating,
        String bookTitle,
        String authorFullName
) {}
