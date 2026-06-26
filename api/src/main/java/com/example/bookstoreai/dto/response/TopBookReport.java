package com.example.bookstoreai.dto.response;

import java.math.BigDecimal;

public record TopBookReport(String title, String author, long totalSold, BigDecimal revenue) {}
