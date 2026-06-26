package com.example.bookstoreai.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReportFilterRequest(
    @NotNull LocalDate dateFrom,
    @NotNull LocalDate dateTo
) {}
