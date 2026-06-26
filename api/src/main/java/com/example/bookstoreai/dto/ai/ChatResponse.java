package com.example.bookstoreai.dto.ai;

import java.util.List;

public record ChatResponse(
    boolean success,
    String summary,
    List<BookItem> books,
    List<ChartData> chartData,
    String userEmail
) {}
