package com.example.bookstoreai.dto.response;

import java.math.BigDecimal;

public record SaleItemResponse(
        String bookTitle,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
