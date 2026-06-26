import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CurrencyPipe } from '@angular/common';
import { BookService } from '../../../../core/services/book.service';
import { BookResponse } from '../../../../models/api.models';

@Component({
  selector: 'app-books-list',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    CurrencyPipe,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule
  ],
  templateUrl: './books-list.component.html',
  styleUrl: './books-list.component.css'
})
export class BooksListComponent implements OnInit {
  private fb = inject(FormBuilder);
  private bookService = inject(BookService);
  private snackBar = inject(MatSnackBar);

  books = signal<BookResponse[]>([]);
  totalElements = signal(0);
  pageSize = signal(10);
  pageIndex = signal(0);

  searchForm = this.fb.group({
    search: ['']
  });

  displayedColumns = ['id', 'title', 'authorFullName', 'genre', 'price', 'stock', 'actions'];

  ngOnInit(): void {
    this.loadBooks();
  }

  onSearch(): void {
    this.pageIndex.set(0);
    this.loadBooks();
  }

  loadBooks(): void {
    const search = this.searchForm.get('search')?.value?.trim() || undefined;
    this.bookService.findAll(this.pageIndex(), this.pageSize(), search).subscribe({
      next: (page) => {
        this.books.set(page.content);
        this.totalElements.set(page.totalElements);
      },
      error: () => {}
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadBooks();
  }

  deleteBook(id: number): void {
    if (!confirm('¿Está seguro de eliminar este libro?')) return;
    this.bookService.delete(id).subscribe({
      next: () => {
        this.snackBar.open('Libro eliminado correctamente', 'Cerrar', { duration: 3000 });
        this.loadBooks();
      },
      error: () => {}
    });
  }
}
