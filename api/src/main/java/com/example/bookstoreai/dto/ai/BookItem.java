package com.example.bookstoreai.dto.ai;

public record BookItem(
        String title,
        String author,
        String genre,
        String price,
        Integer stock
) {}