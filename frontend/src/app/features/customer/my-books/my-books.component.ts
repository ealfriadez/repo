import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BookService } from '../../../core/services/book.service';
import { BookResponse } from '../../../models/api.models';

@Component({
  selector: 'app-my-books',
  standalone: true,
  imports: [
    ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatIconModule,
    MatButtonModule, MatPaginatorModule, MatProgressSpinnerModule, MatSnackBarModule
  ],
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.css'
})
export class MyBooksComponent implements OnInit {
  private fb = inject(FormBuilder);
  private bookService = inject(BookService);
  private snackBar = inject(MatSnackBar);

  books = signal<BookResponse[]>([]);
  totalElements = signal(0);
  page = signal(0);
  loading = signal(false);

  searchForm = this.fb.group({
    search: ['']
  });

  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    this.loading.set(true);
    const search = this.searchForm.get('search')?.value?.trim() || undefined;
    this.bookService.getMyBooks(this.page(), search).subscribe({
      next: (res) => {
        this.books.set(res.content);
        this.totalElements.set(res.totalElements);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  search(): void {
    this.page.set(0);
    this.loadBooks();
  }

  onPage(event: PageEvent): void {
    this.page.set(event.pageIndex);
    this.loadBooks();
  }

  download(book: BookResponse): void {
    this.bookService.downloadBook(book.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = book.slug + '.pdf';
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: () => {}
    });
  }
}
