import { Component, inject, signal, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router, RouterModule } from '@angular/router';
import { environment } from '../../../../../environments/environment';
import { CatalogService } from '../../../../core/services/catalog.service';
import { CartService } from '../../../../core/services/cart.service';
import { AuthService } from '../../../../core/services/auth.service';
import { BookResponse, PageResponse } from '../../../../models/api.models';

@Component({
  selector: 'app-catalog-page',
  standalone: true,
  imports: [
    ReactiveFormsModule, MatCardModule, MatInputModule, MatButtonModule,
    MatIconModule, MatPaginatorModule, MatProgressSpinnerModule,
    MatSnackBarModule, RouterModule
  ],
  templateUrl: './catalog-page.component.html',
  styleUrl: './catalog-page.component.css'
})
export class CatalogPageComponent implements OnInit {
  private fb = inject(FormBuilder);
  private catalogService = inject(CatalogService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  cartService = inject(CartService);
  auth = inject(AuthService);

  apiUrl = environment.apiUrl;
  books = signal<BookResponse[]>([]);
  totalElements = signal(0);
  page = signal(0);
  loading = signal(false);

  searchForm = this.fb.group({
    search: ['']
  });

  genres = ['Terror', 'Novela', 'Realismo magico', 'Cuento', 'Ciencia Ficcion', 'Poesia'];
  selectedGenre = signal<string | null>(null);

  ngOnInit() {
    this.loadBooks();
  }

  loadBooks() {
    this.loading.set(true);
    this.catalogService.searchBooks('', this.page()).subscribe({
      next: (res) => this.updateBookList(res),
      error: () => this.loading.set(false)
    });
  }

  search() {
    const term = this.searchForm.get('search')?.value ?? '';
    this.selectedGenre.set(null);
    this.page.set(0);
    this.loading.set(true);
    this.catalogService.searchBooks(term.trim(), this.page()).subscribe({
      next: (res) => this.updateBookList(res),
      error: () => this.loading.set(false)
    });
  }

  filterByGenre(genre: string) {
    if (this.selectedGenre() === genre) {
      this.clearGenre();
      return;
    }
    this.selectedGenre.set(genre);
    this.searchForm.get('search')?.setValue('');
    this.page.set(0);
    this.loading.set(true);
    this.catalogService.findByGenre(genre, this.page()).subscribe({
      next: (res) => this.updateBookList(res),
      error: () => this.loading.set(false)
    });
  }

  clearGenre() {
    this.selectedGenre.set(null);
    this.page.set(0);
    this.loadBooks();
  }

  onPage(event: PageEvent) {
    this.page.set(event.pageIndex);
    const genre = this.selectedGenre();
    const searchTerm = this.searchForm.get('search')?.value ?? '';
    this.loading.set(true);

    if (genre) {
      this.catalogService.findByGenre(genre, this.page()).subscribe({
        next: (res) => this.updateBookList(res),
        error: () => this.loading.set(false)
      });
    } else if (searchTerm.trim()) {
      this.catalogService.searchBooks(searchTerm.trim(), this.page()).subscribe({
        next: (res) => this.updateBookList(res),
        error: () => this.loading.set(false)
      });
    } else {
      this.loadBooks();
    }
  }

  addToCart(book: BookResponse) {
    if (!this.auth.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    this.cartService.add(book);
    this.snackBar.open('Libro agregado al carrito', 'Cerrar');
  }

  private updateBookList(res: PageResponse<BookResponse>) {
    this.books.set(res.content);
    this.totalElements.set(res.totalElements);
    this.loading.set(false);
  }
}
