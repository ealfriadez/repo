import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { ReportService } from '../../../core/services/report.service';
import { CatalogService } from '../../../core/services/catalog.service';
import { BookService } from '../../../core/services/book.service';
import { MonthlySalesReport, TopBookReport } from '../../../models/report.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-owner-dashboard',
  standalone: true,
  imports: [],
  templateUrl: './owner-dashboard.component.html',
  styleUrl: './owner-dashboard.component.css'
})
export class OwnerDashboardComponent implements OnInit {
  private reportService = inject(ReportService);
  private bookService = inject(BookService);

  monthlySales = signal<MonthlySalesReport[]>([]);
  topBooks = signal<TopBookReport[]>([]);
  librosActivos = signal(0);

  ventasDelMes = computed(() => this.monthlySales().reduce((sum, m) => sum + m.totalSales, 0));
  ingresosDelMes = computed(() => {
    const total = this.monthlySales().reduce((sum, m) => sum + m.totalRevenue, 0);
    return total.toFixed(2);
  });

  ngOnInit(): void {
    this.loadDashboard();
  }

  private loadDashboard(): void {
    const now = new Date();
    const firstDay = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-01`;
    const lastDay = new Date(now.getFullYear(), now.getMonth() + 1, 0);
    const to = `${lastDay.getFullYear()}-${String(lastDay.getMonth() + 1).padStart(2, '0')}-${String(lastDay.getDate()).padStart(2, '0')}`;

    forkJoin({
      monthly: this.reportService.getMonthlySales(firstDay, to),
      top: this.reportService.getTopBooks(firstDay, to, 3),
      books: this.bookService.findAll(0, 1)
    }).subscribe({
      next: ({ monthly, top, books }) => {
        this.monthlySales.set(monthly);
        this.topBooks.set(top);
        this.librosActivos.set(books.totalElements);
      },
      error: () => {
        this.monthlySales.set([]);
        this.topBooks.set([]);
        this.librosActivos.set(0);
      }
    });
  }
}
