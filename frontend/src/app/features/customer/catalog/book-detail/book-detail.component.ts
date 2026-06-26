import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { environment } from '../../../../../environments/environment';
import { BookService } from '../../../../core/services/book.service';
import { ReviewService } from '../../../../core/services/review.service';
import { CartService } from '../../../../core/services/cart.service';
import { AuthService } from '../../../../core/services/auth.service';
import { BookResponse, ReviewResponse } from '../../../../models/api.models';

@Component({
  selector: 'app-book-detail',
  standalone: true,
  imports: [
    ReactiveFormsModule, RouterModule,
    MatCardModule, MatButtonModule, MatIconModule,
    MatChipsModule, MatDividerModule, MatSnackBarModule,
    MatFormFieldModule, MatInputModule
  ],
  templateUrl: './book-detail.component.html',
  styleUrls: ['./book-detail.component.css']
})
export class BookDetailComponent implements OnInit {
  private fb = inject(FormBuilder);
  private bookService = inject(BookService);
  private reviewService = inject(ReviewService);
  private cartService = inject(CartService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  authService = inject(AuthService);
  apiUrl = environment.apiUrl;

  book = signal<BookResponse | null>(null);
  loading = signal(true);
  reviews = signal<ReviewResponse[]>([]);
  myReview = signal<ReviewResponse | null>(null);
  submittingReview = signal(false);

  stars = [1, 2, 3, 4, 5];
  reviewRating = signal(0);
  editingReview = signal(false);

  reviewForm = this.fb.group({
    comment: ['', Validators.required]
  });

  otherReviews = computed(() => {
    const my = this.myReview();
    return this.reviews().filter(r => !my || r.id !== my.id);
  });

  ngOnInit(): void {
    const slug = this.route.snapshot.paramMap.get('slug');
    if (!slug) {
      this.router.navigate(['/catalog']);
      return;
    }

    this.loading.set(true);
    this.bookService.findBySlug(slug).subscribe({
      next: (book) => {
        this.book.set(book);
        this.loading.set(false);
        this.loadReviews(book.id);
      },
      error: () => {
        this.loading.set(false);
        this.router.navigate(['/catalog']);
      }
    });
  }

  private loadReviews(bookId: number): void {
    this.reviewService.getByBook(bookId).subscribe({
      next: (list) => {
        const reviews = Array.isArray(list) ? list : [];
        this.reviews.set(reviews);
        const email = this.authService.email();
        if (email) {
          this.myReview.set(reviews.find(r => r.userName === email) ?? null);
        }
      },
      error: () => this.reviews.set([])
    });
  }

  addToCart(): void {
    const b = this.book();
    if (b) {
      this.cartService.add(b);
      this.snackBar.open('Libro agregado al carrito', 'Cerrar', { duration: 3000 });
    }
  }

  submitReview(): void {
    const b = this.book();
    const comment = this.reviewForm.get('comment')?.value?.trim() ?? '';
    if (!b || this.reviewRating() === 0 || !comment) return;

    this.submittingReview.set(true);
    const request = { rating: this.reviewRating(), comment, bookId: b.id };
    const my = this.myReview();
    const action = (my && this.editingReview())
      ? this.reviewService.update(my.id, request)
      : this.reviewService.create(request);

    action.subscribe({
      next: () => {
        this.snackBar.open(this.editingReview() ? 'Reseña actualizada' : 'Reseña publicada', 'Cerrar', { duration: 3000 });
        this.reviewRating.set(0);
        this.reviewForm.get('comment')?.setValue('');
        this.editingReview.set(false);
        this.submittingReview.set(false);
        this.loadReviews(b.id);
      },
      error: () => this.submittingReview.set(false)
    });
  }

  editReview(): void {
    const my = this.myReview();
    if (my) {
      this.reviewRating.set(my.rating);
      this.reviewForm.get('comment')?.setValue(my.comment);
      this.editingReview.set(true);
    }
  }

  deleteReview(): void {
    const my = this.myReview();
    const b = this.book();
    if (!my || !b) return;
    this.reviewService.delete(my.id).subscribe({
      next: () => {
        this.snackBar.open('Reseña eliminada', 'Cerrar', { duration: 3000 });
        this.myReview.set(null);
        this.editingReview.set(false);
        this.reviewRating.set(0);
        this.reviewForm.get('comment')?.setValue('');
        this.loadReviews(b.id);
      },
      error: () => {}
    });
  }

  cancelEdit(): void {
    this.editingReview.set(false);
    this.reviewRating.set(0);
    this.reviewForm.get('comment')?.setValue('');
  }

  setRating(value: number): void { this.reviewRating.set(value); }

  goBack(): void { this.router.navigate(['/catalog']); }

  getInitials(name: string): string {
    if (!name) return '?';
    return name.split('@')[0].substring(0, 2).toUpperCase();
  }

  getDisplayName(name: string): string {
    if (!name) return 'Anónimo';
    if (name.includes('@')) return name.split('@')[0];
    return name;
  }
}
