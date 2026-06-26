package com.example.bookstoreai.dto.response;

public record AuthorResponse(
        Long id,
        String firstName,
        String lastName,
        String nationality
) {}
