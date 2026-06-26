import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { SaleService } from '../../../../core/services/sale.service';
import { AuthService } from '../../../../core/services/auth.service';
import { SaleResponse } from '../../../../models/api.models';

@Component({
  selector: 'app-sales-list',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CurrencyPipe,
    DatePipe,
    MatExpansionModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './sales-list.component.html',
  styleUrl: './sales-list.component.css'
})
export class SalesListComponent implements OnInit {
  private saleService = inject(SaleService);
  private snackBar = inject(MatSnackBar);
  private fb = inject(FormBuilder);
  auth = inject(AuthService);

  sales = signal<SaleResponse[]>([]);
  totalElements = signal(0);
  pageSize = signal(10);
  pageIndex = signal(0);

  filterForm = this.fb.group({
    dateFrom: [new Date()],
    dateTo: [new Date()]
  });

  itemColumns = ['bookTitle', 'quantity', 'unitPrice', 'subtotal'];

  ngOnInit(): void {
    this.loadSales();
  }

  loadSales(): void {
    const filters: { dateFrom?: string; dateTo?: string } = {};
    const dateFrom = this.filterForm.get('dateFrom')?.value;
    const dateTo = this.filterForm.get('dateTo')?.value;
    if (dateFrom) filters.dateFrom = dateFrom.toISOString().split('T')[0];
    if (dateTo) filters.dateTo = dateTo.toISOString().split('T')[0];

    this.saleService.findAll(this.pageIndex(), this.pageSize(), filters).subscribe({
      next: (page) => {
        this.sales.set(page.content);
        this.totalElements.set(page.totalElements);
      },
      error: () => {}
    });
  }

  applyFilters(): void {
    this.pageIndex.set(0);
    this.loadSales();
  }

  clearFilters(): void {
    this.filterForm.patchValue({
      dateFrom: new Date(),
      dateTo: new Date()
    });
    this.pageIndex.set(0);
    this.loadSales();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadSales();
  }
}
