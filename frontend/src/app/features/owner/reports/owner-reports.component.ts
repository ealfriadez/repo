import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData } from 'chart.js';
import { ReportService } from '../../../core/services/report.service';
import { ChatService } from '../../../core/services/chat.service';
import { MonthlySalesReport, TopBookReport } from '../../../models/report.model';
import { ChatResponse, ChartData as AiChartData } from '../../../models/ai.models';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-owner-reports',
  standalone: true,
  imports: [
    ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatProgressSpinnerModule,
    MatDatepickerModule, MatNativeDateModule, MatSnackBarModule, BaseChartDirective
  ],
  templateUrl: './owner-reports.component.html',
  styleUrl: './owner-reports.component.css'
})
export class OwnerReportsComponent implements OnInit {
  private reportService = inject(ReportService);
  private chatService = inject(ChatService);
  private fb = inject(FormBuilder);

  loading = signal(false);
  mode = signal<'filters' | 'ia'>('filters');
  aiResponse = signal('');
  aiLoading = signal(false);
  aiChartData = signal<AiChartData[]>([]);

  aiChartDataFormatted = computed<ChartData<'bar'>>(() => ({
    labels: this.aiChartData().map(d => d.label),
    datasets: [{
      label: 'Ingresos S/.',
      data: this.aiChartData().map(d => d.value),
      backgroundColor: '#e2b04a',
      borderRadius: 8
    }]
  }));

  filterForm = this.fb.group({
    dateFrom: [new Date()],
    dateTo: [new Date()]
  });

  aiForm = this.fb.group({
    query: ['']
  });

  monthlySales = signal<MonthlySalesReport[]>([]);
  topBooks = signal<TopBookReport[]>([]);

  hasData = computed(() => this.monthlySales().length > 0 || this.topBooks().length > 0);

  get dateError(): boolean {
    const from = this.filterForm.get('dateFrom')?.value;
    const to = this.filterForm.get('dateTo')?.value;
    return !!(from && to && from > to);
  }

  monthlySalesData = computed<ChartData<'bar'>>(() => {
    const monthNames = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
    return {
      labels: this.monthlySales().map(s => `${monthNames[s.month - 1]} ${s.year}`),
      datasets: [{
        label: 'Ventas',
        data: this.monthlySales().map(s => s.totalSales),
        backgroundColor: '#e2b04a',
        borderRadius: 8
      }]
    };
  });

  topBooksData = computed<ChartData<'bar', number[], string>>(() => ({
    labels: this.topBooks().map(b => b.title.length > 25 ? b.title.substring(0, 25) + '...' : b.title),
    datasets: [{
      label: 'Vendidos',
      data: this.topBooks().map(b => b.totalSold),
      backgroundColor: '#e2b04a',
      borderRadius: 4
    }]
  }));

  chartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    plugins: {
      legend: { display: false }
    },
    scales: {
      y: { beginAtZero: true }
    }
  };

  horizontalChartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    indexAxis: 'y',
    plugins: {
      legend: { display: false }
    },
    scales: {
      x: { beginAtZero: true }
    }
  };

  ngOnInit(): void {
    this.loadReports();
  }

  generateReport(): void {
    if (this.dateError) return;
    this.loadReports();
  }

  clearFilters(): void {
    this.filterForm.patchValue({
      dateFrom: new Date(),
      dateTo: new Date()
    });
    this.loadReports();
  }

  askAI(): void {
    const query = this.aiForm.get('query')?.value?.trim() ?? '';
    if (!query) return;
    this.aiLoading.set(true);
    this.aiResponse.set('');
    this.aiChartData.set([]);
    this.chatService.chat(query, 'owner-reports').subscribe({
      next: (res: ChatResponse) => {
        this.aiResponse.set(res.summary || 'Sin respuesta');
        this.aiChartData.set(res.chartData?.length ? res.chartData : []);
        this.aiLoading.set(false);
      },
      error: () => {
        this.aiLoading.set(false);
      }
    });
  }

  private toDateString(date: Date): string {
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
  }

  private loadReports(): void {
    this.loading.set(true);
    const from = this.toDateString(this.filterForm.get('dateFrom')!.value!);
    const to = this.toDateString(this.filterForm.get('dateTo')!.value!);
    forkJoin({
      monthly: this.reportService.getMonthlySales(from, to),
      top: this.reportService.getTopBooks(from, to, 10)
    }).subscribe({
      next: ({ monthly, top }) => {
        this.monthlySales.set(monthly);
        this.topBooks.set(top);
        this.loading.set(false);
      },
      error: () => {
        this.monthlySales.set([]);
        this.topBooks.set([]);
        this.loading.set(false);
      }
    });
  }

}
