package com.example.bookstoreai.tools;

import com.example.bookstoreai.dto.response.MonthlySalesReport;
import com.example.bookstoreai.dto.response.TopBookReport;
import com.example.bookstoreai.service.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReportTool {

    private final IReportService reportService;

    @Tool(description = "Genera reporte de ventas de la libreria. Parametros opcionales: dateFrom y dateTo en formato yyyy-MM-dd")
    public String generateSalesReport(String dateFrom, String dateTo) {
        try {
            LocalDate from = (dateFrom != null && !dateFrom.isBlank())
                    ? LocalDate.parse(dateFrom) : LocalDate.now().minusMonths(6);
            LocalDate to = (dateTo != null && !dateTo.isBlank())
                    ? LocalDate.parse(dateTo) : LocalDate.now();

            List<MonthlySalesReport> monthlySales = reportService.getMonthlySales(from, to);
            List<TopBookReport> topBooks = reportService.getTopBooks(from, to, 5);

            if (monthlySales.isEmpty()) {
                return "No se encontraron ventas en el periodo %s a %s.".formatted(from, to);
            }

            String ventas = monthlySales.stream()
                    .map(m -> "- %02d/%d: %d ventas | S/. %s".formatted(
                            m.month(), m.year(), m.totalSales(), m.totalRevenue().toPlainString()))
                    .collect(Collectors.joining("\n"));

            String tops = topBooks.stream()
                    .map(t -> "- %s: %d vendidos | S/. %s".formatted(
                            t.title(), t.totalSold(), t.revenue().toPlainString()))
                    .collect(Collectors.joining("\n"));

            String chartData = monthlySales.stream()
                    .map(m -> "%02d/%d:%.2f".formatted(m.month(), m.year(), m.totalRevenue()))
                    .collect(Collectors.joining("|"));

            return """
                    REPORTE DE VENTAS (%s a %s)

                    Ventas mensuales:
                    %s

                    Top libros vendidos:
                    %s

                    [CHART_DATA]|%s""".formatted(from, to, ventas, tops, chartData);

        } catch (Exception e) {
            return "Error al generar el reporte: " + e.getMessage();
        }
    }
}
