package com.example.bookstoreai.dto.request;

public record BookFilterRequest(
    String search,
    String genre
) {}
