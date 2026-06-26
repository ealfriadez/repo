package com.example.bookstoreai.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleResponse(
        Long id,
        Long customerId,
        String customerName,
        BigDecimal total,
        LocalDateTime createdAt,
        List<SaleItemResponse> items
) {}
