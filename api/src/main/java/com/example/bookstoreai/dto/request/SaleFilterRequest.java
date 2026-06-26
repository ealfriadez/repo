package com.example.bookstoreai.dto.request;

import java.time.LocalDate;

public record SaleFilterRequest(
    LocalDate dateFrom,
    LocalDate dateTo
) {
    public boolean hasDateRange() {
        return dateFrom != null && dateTo != null;
    }
}
