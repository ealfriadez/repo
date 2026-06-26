package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.response.MonthlySalesReport;
import com.example.bookstoreai.dto.response.TopBookReport;
import com.example.bookstoreai.repository.SaleItemRepository;
import com.example.bookstoreai.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock SaleRepository saleRepository;
    @Mock SaleItemRepository saleItemRepository;

    @InjectMocks
    ReportServiceImpl reportService;

    // fechas reutilizadas en todos los tests
    LocalDate from = LocalDate.of(2025, 1, 1);
    LocalDate to   = LocalDate.of(2025, 3, 31);

    @Test
    void getMonthlySales_ok() {
        var rows = Collections.singletonList(new Object[]{ 2, 2025, 10L, new BigDecimal("500.00") });
        when(saleRepository.getMonthlySales(from, to)).thenReturn(rows);

        List<MonthlySalesReport> result = reportService.getMonthlySales(from, to);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).month()).isEqualTo(2);
        assertThat(result.get(0).totalRevenue()).isEqualByComparingTo("500.00");
    }

    @Test
    void getMonthlySales_empty() {
        when(saleRepository.getMonthlySales(from, to)).thenReturn(List.of());

        assertThat(reportService.getMonthlySales(from, to)).isEmpty();
    }

    @Test
    void getMonthlySales_multipleMonths_preservesOrder() {
        var rows = List.of(
                new Object[]{ 1, 2025, 5L, new BigDecimal("200.00") },
                new Object[]{ 2, 2025, 8L, new BigDecimal("350.00") }
        );
        when(saleRepository.getMonthlySales(from, to)).thenReturn(rows);

        List<MonthlySalesReport> result = reportService.getMonthlySales(from, to);

        assertThat(result.get(0).month()).isEqualTo(1);
        assertThat(result.get(1).month()).isEqualTo(2);
    }

    @Test
    void getTopBooks_ok() {
        var rows = Collections.singletonList(
                new Object[]{ "Cien años de soledad", "Garcia Marquez", 15L, new BigDecimal("825.00") }
        );
        when(saleItemRepository.getTopBooks(eq(from), eq(to), any(PageRequest.class))).thenReturn(rows);

        List<TopBookReport> result = reportService.getTopBooks(from, to, 5);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Cien años de soledad");
        assertThat(result.get(0).totalSold()).isEqualTo(15L);
    }

    @Test
    void getTopBooks_empty() {
        when(saleItemRepository.getTopBooks(eq(from), eq(to), any(PageRequest.class))).thenReturn(List.of());

        assertThat(reportService.getTopBooks(from, to, 5)).isEmpty();
    }

    @Test
    void getTopBooks_limitApplied() {
        var rows = List.of(
                new Object[]{ "Libro 1", "Autor 1", 10L, new BigDecimal("100.00") },
                new Object[]{ "Libro 2", "Autor 2", 8L,  new BigDecimal("80.00") }
        );
        when(saleItemRepository.getTopBooks(eq(from), eq(to), eq(PageRequest.of(0, 2)))).thenReturn(rows);

        assertThat(reportService.getTopBooks(from, to, 2)).hasSize(2);
    }
}
