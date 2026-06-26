import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CartService } from '../../../core/services/cart.service';
import { SaleService } from '../../../core/services/sale.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    RouterLink, MatCardModule, MatInputModule, MatButtonModule,
    MatIconModule, MatTableModule, MatDividerModule, MatProgressSpinnerModule, MatSnackBarModule
  ],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {
  private saleService = inject(SaleService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  cartService = inject(CartService);
  saving = signal(false);
  columns = ['book', 'price', 'quantity', 'subtotal', 'actions'];

  checkout(): void {
    if (this.cartService.cartItems().length === 0) return;
    this.saving.set(true);

    const items = this.cartService.cartItems().map(i => ({
      bookId: i.book.id,
      quantity: i.quantity
    }));

    this.saleService.create({ items }).subscribe({
      next: () => {
        this.snackBar.open('Compra realizada correctamente', 'Cerrar');
        this.cartService.clear();
        this.router.navigate(['/customer/my-purchases']);
      },
      error: () => {
        this.saving.set(false);
      }
    });
  }
}
