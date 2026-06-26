package com.example.bookstoreai.dto.response;

public record AuthResponse(
        String token,
        String email,
        String role
) {}
