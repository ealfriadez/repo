package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.response.MonthlySalesReport;
import com.example.bookstoreai.dto.response.TopBookReport;
import com.example.bookstoreai.repository.SaleItemRepository;
import com.example.bookstoreai.repository.SaleRepository;
import com.example.bookstoreai.service.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements IReportService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;

    @Override
    public List<MonthlySalesReport> getMonthlySales(LocalDate dateFrom, LocalDate dateTo) {
        return saleRepository.getMonthlySales(dateFrom, dateTo).stream()
                .map(row -> new MonthlySalesReport(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).intValue(),
                        ((Number) row[2]).longValue(),
                        new BigDecimal(row[3].toString())
                )).toList();
    }

    @Override
    public List<TopBookReport> getTopBooks(LocalDate dateFrom, LocalDate dateTo, int limit) {
        return saleItemRepository.getTopBooks(dateFrom, dateTo, PageRequest.of(0, limit)).stream()
                .map(row -> new TopBookReport(
                        (String) row[0],
                        (String) row[1],
                        ((Number) row[2]).longValue(),
                        new BigDecimal(row[3].toString())
                )).toList();
    }
}
