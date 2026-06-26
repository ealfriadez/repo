import { Component, OnInit, inject, signal } from '@angular/core';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ReviewService } from '../../../../core/services/review.service';
import { BookService } from '../../../../core/services/book.service';
import { BookResponse } from '../../../../models/api.models';

@Component({
  selector: 'app-review-form',
  standalone: true,
  imports: [
    ReactiveFormsModule, RouterLink,
    MatCardModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatAutocompleteModule,
    MatButtonModule, MatIconModule, MatSnackBarModule
  ],
  templateUrl: './review-form.component.html',
  styleUrl: './review-form.component.css'
})
export class ReviewFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private reviewService = inject(ReviewService);
  private bookService = inject(BookService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private snackBar = inject(MatSnackBar);

  isEditMode = signal(false);
  books = signal<BookResponse[]>([]);
  filteredBooks = signal<BookResponse[]>([]);
  selectedBookId = signal(0);
  ratings = [1, 2, 3, 4, 5];
  private reviewId: number | null = null;

  reviewForm = this.fb.group({
    bookSearch: [''],
    rating: [5, Validators.required],
    comment: ['', Validators.required]
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    // Primero carga libros, luego si es edición carga la reseña
    this.bookService.findAll(0, 100).subscribe({
      next: (page) => {
        this.books.set(page.content);
        this.filteredBooks.set(page.content);

        if (id) {
          this.isEditMode.set(true);
          this.reviewId = +id;
          this.reviewService.findById(+id).subscribe({
            next: (review) => {
              this.reviewForm.patchValue({ comment: review.comment, rating: review.rating });
              this.selectedBookId.set(review.bookId);
              const book = page.content.find(b => b.id === review.bookId);
              if (book) this.reviewForm.get('bookSearch')?.setValue(book.title);
            },
            error: () => {}
          });
        }
      },
      error: () => {}
    });
  }

  filterBooks(text: string): void {
    const lower = text.toLowerCase();
    this.filteredBooks.set(this.books().filter(b => b.title.toLowerCase().includes(lower)));
  }

  onBookSearchInput(): void {
    const text = this.reviewForm.get('bookSearch')?.value ?? '';
    this.filterBooks(text);
  }

  selectBook(book: BookResponse): void {
    this.selectedBookId.set(book.id);
    this.reviewForm.get('bookSearch')?.setValue(book.title);
  }

  getStarsArray(n: number): number[] { return Array(n).fill(0); }
  getEmptyStarsArray(n: number): number[] { return Array(5 - n).fill(0); }

  saveReview(): void {
    if (this.reviewForm.invalid || this.selectedBookId() === 0) return;
    const formValue = this.reviewForm.getRawValue();
    const reviewData = {
      comment: formValue.comment ?? '',
      rating: formValue.rating ?? 5,
      bookId: this.selectedBookId()
    };
    if (this.isEditMode() && this.reviewId) {
      this.reviewService.update(this.reviewId, reviewData).subscribe({
        next: () => {
          this.snackBar.open('Reseña actualizada correctamente', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/customer/reviews']);
        },
        error: () => {}
      });
    } else {
      this.reviewService.create(reviewData).subscribe({
        next: () => {
          this.snackBar.open('Reseña creada correctamente', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/customer/reviews']);
        },
        error: () => {}
      });
    }
  }
}
