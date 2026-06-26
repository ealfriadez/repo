package com.example.bookstoreai.service;

import com.example.bookstoreai.dto.response.MonthlySalesReport;
import com.example.bookstoreai.dto.response.TopBookReport;

import java.time.LocalDate;
import java.util.List;

public interface IReportService {
    List<MonthlySalesReport> getMonthlySales(LocalDate dateFrom, LocalDate dateTo);
    List<TopBookReport> getTopBooks(LocalDate dateFrom, LocalDate dateTo, int limit);
}
