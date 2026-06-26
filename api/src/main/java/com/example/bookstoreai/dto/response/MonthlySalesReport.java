package com.example.bookstoreai.dto.response;

import java.math.BigDecimal;

public record MonthlySalesReport(int month, int year, long totalSales, BigDecimal totalRevenue) {}
