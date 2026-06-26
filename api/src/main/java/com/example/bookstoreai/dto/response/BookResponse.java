package com.example.bookstoreai.dto.response;

import java.math.BigDecimal;

public record BookResponse(
        Long id,
        String title,
        String slug,
        String genre,
        BigDecimal price,
        Integer stock,
        String description,
        String authorFullName,
        String imageUrl,
        boolean hasFile
) {}
