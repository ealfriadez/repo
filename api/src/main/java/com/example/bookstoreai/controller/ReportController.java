package com.example.bookstoreai.controller;

import com.example.bookstoreai.dto.request.ReportFilterRequest;
import com.example.bookstoreai.dto.response.MonthlySalesReport;
import com.example.bookstoreai.dto.response.TopBookReport;
import com.example.bookstoreai.service.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('OWNER')")
public class ReportController {

    private final IReportService reportService;

    @GetMapping("/monthly-sales")
    public ResponseEntity<List<MonthlySalesReport>> getMonthlySales(ReportFilterRequest filter) {
        return ResponseEntity.ok(reportService.getMonthlySales(filter.dateFrom(), filter.dateTo()));
    }

    @GetMapping("/top-books")
    public ResponseEntity<List<TopBookReport>> getTopBooks(
            ReportFilterRequest filter,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(reportService.getTopBooks(filter.dateFrom(), filter.dateTo(), limit));
    }
}
