import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { BookService } from '../../../../core/services/book.service';
import { ReviewService } from '../../../../core/services/review.service';
import { BookResponse, ReviewResponse } from '../../../../models/api.models';

@Component({
  selector: 'app-review-page',
  standalone: true,
  imports: [
    ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule,
    MatInputModule, MatSelectModule, MatPaginatorModule, MatProgressSpinnerModule,
    MatSnackBarModule, MatDividerModule
  ],
  templateUrl: './review-page.component.html',
  styleUrl: './review-page.component.css'
})
export class ReviewPageComponent implements OnInit {
  private fb = inject(FormBuilder);
  private bookService = inject(BookService);
  private reviewService = inject(ReviewService);
  private snackBar = inject(MatSnackBar);

  books = signal<BookResponse[]>([]);
  totalBooks = signal(0);
  page = signal(0);
  loadingBooks = signal(false);

  selectedBook = signal<BookResponse | null>(null);
  bookReviews = signal<ReviewResponse[]>([]);
  loadingReviews = signal(false);

  showForm = signal(false);
  saving = signal(false);
  ratings = [1, 2, 3, 4, 5];

  reviewForm = this.fb.group({
    comment: ['', Validators.required],
    rating: [5, Validators.required]
  });

  ngOnInit() { this.loadBooks(); }

  loadBooks() {
    this.loadingBooks.set(true);
    this.bookService.findAll(this.page(), 8).subscribe({
      next: (res) => {
        this.books.set(res.content);
        this.totalBooks.set(res.totalElements);
        this.loadingBooks.set(false);
      },
      error: () => this.loadingBooks.set(false)
    });
  }

  onPageChange(event: PageEvent) {
    this.page.set(event.pageIndex);
    this.loadBooks();
  }

  selectBook(book: BookResponse) {
    this.selectedBook.set(book);
    this.showForm.set(false);
    this.loadReviews(book.id);
  }

  loadReviews(bookId: number) {
    this.loadingReviews.set(true);
    this.reviewService.getByBook(bookId).subscribe({
      next: (reviews) => {
        this.bookReviews.set(reviews);
        this.loadingReviews.set(false);
      },
      error: () => this.loadingReviews.set(false)
    });
  }

  openForm() {
    const book = this.selectedBook();
    if (!book) return;
    this.reviewForm.patchValue({ comment: '', rating: 5 });
    this.showForm.set(true);
  }

  cancelForm() {
    this.showForm.set(false);
  }

  submitReview() {
    if (this.reviewForm.invalid) return;
    const book = this.selectedBook();
    if (!book) return;
    const formValue = this.reviewForm.getRawValue();
    const request = {
      comment: formValue.comment ?? '',
      rating: formValue.rating ?? 5,
      bookId: book.id
    };

    this.saving.set(true);
    this.reviewService.create(request).subscribe({
      next: () => {
        this.snackBar.open('Reseña publicada', 'Cerrar', { duration: 3000 });
        this.saving.set(false);
        this.showForm.set(false);
        this.loadReviews(this.selectedBook()!.id);
      },
      error: () => {
        this.saving.set(false);
      }
    });
  }

  getStars(n: number): number[] { return Array(n).fill(0); }
  getEmptyStars(n: number): number[] { return Array(5 - n).fill(0); }
}
