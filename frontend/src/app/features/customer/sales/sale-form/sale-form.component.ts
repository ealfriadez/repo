import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { CurrencyPipe } from '@angular/common';
import { BookService } from '../../../../core/services/book.service';
import { SaleService } from '../../../../core/services/sale.service';
import { BookResponse, SaleItemRequest } from '../../../../models/api.models';

interface CartItem {
  bookId: number;
  bookTitle: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

@Component({
  selector: 'app-sale-form',
  standalone: true,
  imports: [
    ReactiveFormsModule, RouterLink, CurrencyPipe,
    MatCardModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule,
    MatTableModule, MatSnackBarModule
  ],
  templateUrl: './sale-form.component.html',
  styleUrl: './sale-form.component.css'
})
export class SaleFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private bookService = inject(BookService);
  private saleService = inject(SaleService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  books = signal<BookResponse[]>([]);
  cartItems = signal<CartItem[]>([]);
  cartColumns = ['bookTitle', 'quantity', 'unitPrice', 'subtotal', 'actions'];

  total = computed(() =>
    this.cartItems().reduce((sum, item) => sum + item.subtotal, 0)
  );

  saleForm = this.fb.group({
    selectedBookId: [0],
    quantity: [1]
  });

  ngOnInit(): void {
    this.bookService.findAll(0, 100).subscribe({
      next: (page) => this.books.set(page.content),
      error: () => {}
    });
  }

  addToCart(): void {
    const bookId = this.saleForm.get('selectedBookId')?.value ?? 0;
    const quantity = this.saleForm.get('quantity')?.value ?? 1;
    const book = this.books().find(b => b.id === bookId);
    if (!book) return;

    const existing = this.cartItems().find(i => i.bookId === book.id);
    if (existing) {
      this.cartItems.update(items =>
        items.map(i => i.bookId === book.id
          ? { ...i, quantity: i.quantity + quantity, subtotal: (i.quantity + quantity) * i.unitPrice }
          : i
        )
      );
    } else {
      this.cartItems.update(items => [...items, {
        bookId: book.id,
        bookTitle: book.title,
        quantity,
        unitPrice: book.price,
        subtotal: book.price * quantity
      }]);
    }
    this.saleForm.patchValue({ selectedBookId: 0, quantity: 1 });
  }

  removeFromCart(bookId: number): void {
    this.cartItems.update(items => items.filter(i => i.bookId !== bookId));
  }

  placeOrder(): void {
    if (this.cartItems().length === 0) {
      this.snackBar.open('Agregue al menos un libro', 'Cerrar', { duration: 3000 });
      return;
    }
    const items: SaleItemRequest[] = this.cartItems().map(i => ({
      bookId: i.bookId,
      quantity: i.quantity
    }));
    this.saleService.create({ items }).subscribe({
      next: () => {
        this.snackBar.open('Venta registrada correctamente', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/customer/my-purchases']);
      },
      error: () => {}
    });
  }
}
