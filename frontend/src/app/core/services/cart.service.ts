import { Injectable, signal, computed } from '@angular/core';
import { BookResponse } from '../../models/api.models';

export interface CartItem {
  book: BookResponse;
  quantity: number;
}

@Injectable({ providedIn: 'root' })
export class CartService {
  private items = signal<CartItem[]>([]);

  readonly cartItems = this.items.asReadonly();
  readonly itemCount = computed(() => this.items().reduce((sum, i) => sum + i.quantity, 0));
  readonly total = computed(() => this.items().reduce((sum, i) => sum + i.book.price * i.quantity, 0));

  add(book: BookResponse, quantity = 1) {
    this.items.update(items => {
      const existing = items.find(i => i.book.id === book.id);
      if (existing) {
        return items.map(i => i.book.id === book.id
          ? { ...i, quantity: i.quantity + quantity }
          : i
        );
      }
      return [...items, { book, quantity }];
    });
  }

  updateQuantity(bookId: number, quantity: number) {
    if (quantity <= 0) { this.remove(bookId); return; }
    this.items.update(items => items.map(i => i.book.id === bookId ? { ...i, quantity } : i));
  }

  remove(bookId: number) {
    this.items.update(items => items.filter(i => i.book.id !== bookId));
  }

  clear() {
    this.items.set([]);
  }
}
