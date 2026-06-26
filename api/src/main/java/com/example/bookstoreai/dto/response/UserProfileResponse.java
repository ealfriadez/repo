package com.example.bookstoreai.dto.response;

public record UserProfileResponse(
    String firstName,
    String lastName,
    String phone,
    String roleName
) {}
